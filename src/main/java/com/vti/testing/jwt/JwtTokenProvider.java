package com.vti.testing.jwt;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.jsonwebtoken.*;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import static java.util.Arrays.stream;

@Component
@Getter
public class JwtTokenProvider {
	private final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
//	@Value("${jwt.JWT_SECRET}")
	private final String JWT_SECRET = "kyl2803";
//	 @Value("${jwt.JWT_EXPIRATION}")
	 private  Long jwtExpiration = 15000L ; // 15s
//	@Value("${jwt.JWT_REFRESH_EXPIRATION}")
	private  Long jwtRefreshExpiration = 3600000L; // 1 hour
	private final String CLAIM_NAME = "Roles";
	private  final String PREFIX_TOKEN = "Bearer " ;
    public void generateTokenForClient(HttpServletResponse response, UserDetails userDetails) throws IOException {
    	log.info("User name là : " + userDetails.getUsername());
		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    	String accessToken = Jwts.builder()
    			// Set roles of UserDetails in payload
				.claim(CLAIM_NAME , roles)
    			.setSubject(userDetails.getUsername())
    			.setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
    			.signWith(SignatureAlgorithm.HS512, JWT_SECRET)
    			.compact();
		String refreshToken = Jwts.builder()
				.claim(CLAIM_NAME , roles)
				.setSubject(userDetails.getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpiration))
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET)
				.compact();
		// init obj JwtResponse with accessToken & refreshToken
		JwtResponse jwtResponse = new JwtResponse(accessToken,refreshToken);
		// convert obj -> json with jackson:
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			// obj -> String
		String json = ow.writeValueAsString(jwtResponse);
			// set content type of response -> json
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(json);
	}
    public String getUserNameByJWT(String jwt)  {
    	if(jwt == null) {
    		return null;
    	}
    	// Parse the token
		return Jwts.parser()
				.setSigningKey(JWT_SECRET)
				.parseClaimsJws(jwt)
				.getBody()
				.getSubject();
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