package com.vti.testing.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.vti.testing.service.CustomUserDetails;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class CustomAuthenFilter extends UsernamePasswordAuthenticationFilter {
	
	private static final Logger log = LoggerFactory.getLogger(CustomAuthenFilter.class);	
	
	static final String SECRET = "kyl2803";
	
	private static final Long EXPRATION = 86400000L ;

	
	private final AuthenticationManager manager;
	public CustomAuthenFilter(AuthenticationManager manager) {
		this.manager = manager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		String userName = request.getParameter("userName");
		String passWord = request.getParameter("password");
		log.info("User : " + userName);
		UsernamePasswordAuthenticationToken userToken = 
				new UsernamePasswordAuthenticationToken(userName, passWord);
		return manager.authenticate(userToken);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
			CustomUserDetails user =  (CustomUserDetails) authentication.getPrincipal();
//			log.info("User: " + user.getUsername() + user.getPassword());
			
			Algorithm algorithm = Algorithm.HMAC256(SECRET);
			@SuppressWarnings("unchecked")
			Collection<GrantedAuthority> auths = (Collection<GrantedAuthority>) user.getAuthorities();
			// Create jwt with logging user's infor
			String jwt = JWT.create()
					.withSubject(user.getUsername())
					.withExpiresAt(new Date(System.currentTimeMillis() + EXPRATION))
					.withIssuer(request.getRequestURL().toString())
					.withClaim("Role", 
						auths.stream()
						// không hiểu GrantedAuthority:: getAuthority
						.map(GrantedAuthority:: getAuthority).collect(Collectors.toList()))
					.sign(algorithm);
			
			// New JWT when jwt hết hạn
			String newJwt = JWT.create()
					.withSubject(user.getUsername())
					// New JWT  có hsd dài hơn jwt 3600 * 1000 mls
					.withExpiresAt(new Date(System.currentTimeMillis() + EXPRATION +  1000))
					.withIssuer(request.getRequestURL().toString())
					.sign(algorithm);
			response.setHeader("AccessToken", jwt);
			response.setHeader("RefreshToken", newJwt);
	
	}
}
