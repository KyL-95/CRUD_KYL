package com.vti.testing.filter;


import com.vti.testing.jwt.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
public class CustomAuthorFilter extends OncePerRequestFilter{
	private final JwtTokenProvider jwtTokenProvider;
	public CustomAuthorFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	private  final Logger log = LoggerFactory.getLogger(CustomAuthorFilter.class);
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			if(authorHeader != null && authorHeader.startsWith(jwtTokenProvider.getPREFIX_TOKEN())) {
				String token = null;
				try {
					token = authorHeader.substring(jwtTokenProvider.getPREFIX_TOKEN().length());
					String userName = jwtTokenProvider.getUserNameByJWT(token);
					Collection<? extends GrantedAuthority> authorities = jwtTokenProvider
							.getRolesByToken(token);
					log.info("authors : " + authorities);
					UsernamePasswordAuthenticationToken authenticationToken =
							new UsernamePasswordAuthenticationToken(userName, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request, response);
				} catch (ExpiredJwtException e) {
					log.info("Token has been expired");
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Token has been expired");
				} catch (MalformedJwtException e) {
					log.info("Token has been invalid");
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Token has been invalid");
				}
			}
			else {
				filterChain.doFilter(request, response);
			}

	}

}
