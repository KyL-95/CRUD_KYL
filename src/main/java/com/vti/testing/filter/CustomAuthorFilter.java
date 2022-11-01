package com.vti.testing.filter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import antlr.collections.List;

public class CustomAuthorFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getServletPath().equals("/user-login")) {
			filterChain.doFilter(request, response);
		}else {
			String authorHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			if(authorHeader != null && authorHeader.startsWith("Bearer ")) {
				try {
					String token = authorHeader.substring("Bearer ".length());
					Algorithm algorithm = Algorithm.HMAC256(CustomAuthenFilter.SECRET); 
					JWTVerifier jwtVerifier = JWT.require(algorithm).build();
					DecodedJWT decodedJWT = jwtVerifier.verify(token);
					String userName = decodedJWT.getSubject();
//					List roles = (List) decodedJWT.getClaim("Roles");
					String [] roles = decodedJWT.getClaim("Roles").asArray(String.class);
					java.util.List<String> roleList = Arrays.asList(roles);
					
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
					// convert String -> SimpleGrantedAuthority
					roleList.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
					UsernamePasswordAuthenticationToken authenticationToken = 
							new UsernamePasswordAuthenticationToken(userName, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request, response);
					
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JWTVerificationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				filterChain.doFilter(request, response);
			}
		}
	}

}
