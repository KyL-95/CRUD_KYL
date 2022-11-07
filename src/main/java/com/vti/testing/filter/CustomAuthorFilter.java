package com.vti.testing.filter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.vti.testing.login.JwtTokenProvider;

import io.jsonwebtoken.Jwts;

import static java.util.Arrays.stream;


public class CustomAuthorFilter extends OncePerRequestFilter{
	private static final Logger log = LoggerFactory.getLogger(CustomAuthorFilter.class);
	private static final String JWT_SECRET = "kyl2803";
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getServletPath().equals("/user-login")) {
			System.out.println("-----------------Oke---------------");
			filterChain.doFilter(request, response);
		}else {
			String authorHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			if(authorHeader != null && authorHeader.startsWith("Bearer ")) {
				try {
					String token = authorHeader.substring("Bearer ".length());
					String userName = JwtTokenProvider.getUserNameByJWT(token);
					// get user by this userName
					String roles = Jwts.parser().setSigningKey(JWT_SECRET)
							.parseClaimsJws(token).getBody().get("Roles").toString();
					System.err.println("---------------------   " + roles) ;
					Collection<? extends GrantedAuthority> authorities = stream(roles.split(","))
                            .filter(auth -> !auth.trim().isEmpty())
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
					log.info("authors : " + authorities.toString());
					UsernamePasswordAuthenticationToken authenticationToken = 
							new UsernamePasswordAuthenticationToken(userName, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request, response);
					
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (JWTVerificationException e) {
					e.printStackTrace();
				}
			}
			else {
				filterChain.doFilter(request, response);
			}
		}
	}

}
