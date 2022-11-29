package com.vti.testing.filter;


import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vti.testing.jwt.JwtTokenProvider;

import io.jsonwebtoken.Jwts;

import static java.util.Arrays.stream;
public class CustomAuthorFilter extends OncePerRequestFilter{
//	@Autowired
//	private JwtTokenProvider jwtTokenProvider;
	private  final Logger log = LoggerFactory.getLogger(CustomAuthorFilter.class);
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		JwtTokenProvider tokenProvider = new JwtTokenProvider();
		if (request.getServletPath().equals("/login-abc")) {
			System.out.println("-----------------Oke---------------");
			filterChain.doFilter(request, response);
		}else {
			String authorHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			if(authorHeader != null && authorHeader.startsWith(tokenProvider.getPREFIX_TOKEN())) {
				String token = null;
				try {
					token = authorHeader.substring(tokenProvider.getPREFIX_TOKEN().length());
					String userName = tokenProvider.getUserNameByJWT(token);
					Collection<? extends GrantedAuthority> authorities = tokenProvider
							.getRolesByToken(token);
					log.info("authors : " + authorities);
					UsernamePasswordAuthenticationToken authenticationToken =
							new UsernamePasswordAuthenticationToken(userName, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request, response);
				} catch (IllegalArgumentException e) {
					log.error("JWT claims string is empty.");
				} catch (ExpiredJwtException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Token has been expired");
				} catch (MalformedJwtException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Token has been invalid");
				}
			}
			else {
				filterChain.doFilter(request, response);
			}
		}
	}

}
