package com.vti.testing.jwt;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
@Component
public class JwtTokenProvider {
	private final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
//	@Value("${jwt.JWT_SECRET}")
	private String jwtSecret = "kyl2803";
//	 @Value("${jwt.JWT_EXPIRATION}")
	 private  Long jwtExpiration = 15000L ;
//	@Value("${jwt.JWT_REFRESH_EXPIRATION}")
	private  Long jwtRefreshExpiration = 3600000L;
    public void generateTokenForClient(HttpServletResponse response, UserDetails userDetails) throws IOException {
    	log.info("User name là : " + userDetails.getUsername());
		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    	String accessJWT = Jwts.builder()
    			// Set roles of UserDetails in payload
				.claim("Roles" , roles)
    			.setSubject(userDetails.getUsername())
    			.setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
    			.signWith(SignatureAlgorithm.HS512, jwtSecret)
    			.compact();
		String refreshToken = Jwts.builder()
				.claim("Roles" , roles)
				.setSubject(userDetails.getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpiration))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
    	response.getWriter().write("accessJWT			: " + accessJWT + "\n" +
										"refreshToken		: " + refreshToken);
	}

    public  String getUserNameByJWT(String jwt)  {
    	if(jwt == null) {
    		return null;
    	}
    	// Parse the token
		return Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(jwt)
				.getBody()
				.getSubject();
    }
    
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");	
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

}
