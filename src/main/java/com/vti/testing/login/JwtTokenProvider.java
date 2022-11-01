package com.vti.testing.login;

import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import com.vti.testing.service.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

// Class mã hóa thông tin người dùng thành chuỗi JWT
@Component
public class JwtTokenProvider {
	private static final String PREFIX_TOKEN = "Bearer";
	private static final String AUTHORIZATION = "Authorization";
	private static final String JWT_SECRET = "kyl2803";
	 //Thời gian có hiệu lực của chuỗi jwt
    private static final long JWT_EXPIRATION = 864000000L;
    
    public static void addJWTTokenToHeader(HttpServletResponse response, String userName) {
    	String JWT = Jwts.builder()
    			.setSubject(userName)
    			.setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
    			.signWith(SignatureAlgorithm.HS512, JWT_SECRET)
    			.compact();
    	response.addHeader(AUTHORIZATION, PREFIX_TOKEN + " " + JWT);
    }
    
    public static org.springframework.security.core.Authentication getUserByJWT(HttpServletRequest request)  {
    	String token = request.getHeader(AUTHORIZATION);
    	if(token == null) {
    		return null;
    	}
    	// Parse the token
    	String userName = Jwts.parser()
    			.setSigningKey(JWT_SECRET)
    			.parseClaimsJws(token.replace(PREFIX_TOKEN, ""))
    			.getBody()
    			.getSubject();
    	return userName != null ? 
    			new UsernamePasswordAuthenticationToken(userName, null, Collections.emptyList())
    			: null ;
    }

}
