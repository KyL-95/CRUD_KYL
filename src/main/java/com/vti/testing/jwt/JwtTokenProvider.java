package com.vti.testing.jwt;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vti.testing.entity.RefreshToken;
import com.vti.testing.entity.Role;
import com.vti.testing.repository.IUserRepository;
import com.vti.testing.service.interfaces.IRefreshTokenService;
import io.jsonwebtoken.*;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
//	@Autowired
//	private IUserRepository userRepository;
	private final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
	private final String secret;
	private final Long expiration;

	public JwtTokenProvider(JwtProperties properties) {
		this.secret = properties.getSecret();
		this.expiration = properties.getExpiration();
	}

	private final String CLAIM_NAME = "Roles";
	private  final String PREFIX_TOKEN = "Bearer " ;
    public JwtResponse generateTokenForClient(UserDetails userDetails) throws IOException {
		String accessToken = generateAccessToken(userDetails);
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
		return new JwtResponse(accessToken,refreshToken.getToken(),userDetails.getUsername(),userDetails.getAuthorities());
	}
    public String generateAccessToken (UserDetails userDetails){
		return Jwts.builder()
				// Set roles of UserDetails in payload
				.claim(CLAIM_NAME , userDetails.getAuthorities())
				.setSubject(userDetails.getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();

	}
	public String getUserNameByJWT(String jwt)  {
    	if(jwt == null) {
    		return null;
    	}
    	// Parse the token
		Jwt parse = Jwts.parser()
				.setSigningKey(secret)
				.parse(jwt);
		Claims body = (Claims) parse.getBody();
		return body.getSubject();
    }
}