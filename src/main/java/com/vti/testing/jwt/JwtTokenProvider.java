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
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

// Class mã hóa thông tin người dùng thành chuỗi JWT
@Component
public class JwtTokenProvider {
	private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
//	@Value("${jwt.JWT_SECRET}")
	private static String JWT_SECRET  = "kyl2803";
	 //Thời gian có hiệu lực của chuỗi jwt
	 //	private static final long JWT_EXPIRATION = 3600 * 1000 * 24 * 10;
//	 @Value("${jwt.JWT_EXPIRATION}")
	 private static long JWT_EXPIRATION = 600 * 1000;
    public static void generateTokenForClient(HttpServletResponse response, UserDetails userDetails) throws IOException {
    	log.info("User name là : " + userDetails.getUsername());
		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).collect(Collectors.toList());;
    	String accessJWT = Jwts.builder()
    			// Set roles of UserDetails in payload
				.claim("Roles" , roles)
    			.setSubject(userDetails.getUsername())
    			.setExpiration(new Date(System.currentTimeMillis() +  JWT_EXPIRATION))
    			.signWith(SignatureAlgorithm.HS512, JWT_SECRET)
    			.compact();
		String refreshToken = Jwts.builder()
				// Set roles of UserDetails in payload
				.claim("Roles" , roles)
				.setSubject(userDetails.getUsername())
				.setExpiration(new Date(System.currentTimeMillis() +  JWT_EXPIRATION + 3600 * 1000))
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET)
				.compact();
    	response.getWriter().write("accessJWT			: " + accessJWT + "\n" + "refreshToken		: " + refreshToken);


	}

    public static String getUserNameByJWT(String jwt)  {
    	if(jwt == null) {
    		return null;
    	}
    	// Parse the token
    	String userName = Jwts.parser()
    			.setSigningKey(JWT_SECRET)   			
    			.parseClaimsJws(jwt)
    			.getBody()
    			.getSubject();
    	return userName != null ? 
    			userName	
    			: null ;
    }
    
    public static boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
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
