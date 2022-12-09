package com.vti.testing.jwt;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vti.testing.entity.RefreshToken;
import com.vti.testing.entity.Role;
import com.vti.testing.repository.IUserRepository;
import com.vti.testing.service.RefreshTokenService;
import com.vti.testing.service.interfaces.IRefreshTokenService;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import static java.util.Arrays.stream;

@Component
@Getter
public class JwtTokenProvider  {
	@Autowired
	private IRefreshTokenService refreshTokenService;
	@Autowired
	private IUserRepository userRepository;
	private final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
	@Value("${jwt.JWT_SECRET}")
	private String JWT_SECRET ;
	@Value("${jwt.JWT_EXPIRATION}")
	 private Long jwtExpiration  ; // 25min
	@Value("${jwt.JWT_REFRESH_EXPIRATION}")
	private Long jwtRefreshExpiration ; // 1 hour
	private final String CLAIM_NAME = "Roles";
	private  final String PREFIX_TOKEN = "Bearer " ;
    public void generateTokenForClient(HttpServletResponse response, String userName) throws IOException {
    	log.info("User name là : " + userName);
		List<String> roles = userRepository.findByUserName(userName).getRoles().stream()
				.map(Role::getRoleName).collect(Collectors.toList());
//    	String accessToken = Jwts.builder()
//    			// Set roles of UserDetails in payload
//				.claim(CLAIM_NAME , roles)
//    			.setSubject(userName)
//    			.setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
//    			.signWith(SignatureAlgorithm.HS512, JWT_SECRET)
//    			.compact();
		String accessToken = generateAccessToken(userName);
		// init obj JwtResponse with accessToken & refreshToken
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userName);
		JwtResponse jwtResponse = new JwtResponse(accessToken,refreshToken.getToken(),userName,roles);
		// convert obj -> json with jackson:
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			// obj -> String
		String json = ow.writeValueAsString(jwtResponse);
			// set content type of response -> json
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(json);
	}
    public String generateAccessToken (String userName){
		List<String> roles = userRepository.findByUserName(userName).getRoles().stream()
				.map(Role::getRoleName).collect(Collectors.toList());
		return Jwts.builder()
				// Set roles of UserDetails in payload
				.claim(CLAIM_NAME , roles)
				.setSubject(userName)
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET)
				.compact();

	}
	public String getUserNameByJWT(String jwt)  {


    	if(jwt == null) {
    		return null;
    	}
    	// Parse the token
//		Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt).getBody();
//		return claims.getSubject();
		Jwt parse = Jwts.parser()
				.setSigningKey(JWT_SECRET)
				.parse(jwt);
		Claims body = (Claims) parse.getBody();
		String userName = body.getSubject();
		return userName;
    }
//    public boolean validateToken(String authToken) {
//        try {
//            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
//            return true;
//        } catch (MalformedJwtException ex) {
//            log.error("Invalid JWT token");
//        } catch (ExpiredJwtException ex) {
//            log.error("Expired JWT token");
//        } catch (UnsupportedJwtException ex) {
//            log.error("Unsupported JWT token");
//        } catch (IllegalArgumentException ex) {
//            log.error("JWT claims string is empty.");
//        }
//        return false;
//    }
	public Collection<? extends GrantedAuthority> getRolesByToken(String token){
		String roles = Jwts.parser().setSigningKey(JWT_SECRET)
				.parseClaimsJws(token).getBody().get(CLAIM_NAME).toString();
		roles = roles.substring(1, roles.length() - 1); // 2 ngày debug mới nghĩ ra dòng này :(((
		System.err.println("----------sub-----------   " + roles);
		return stream(roles.split(", "))
				.filter(auth -> !auth.trim().isEmpty())
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}


}