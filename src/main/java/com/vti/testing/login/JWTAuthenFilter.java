package com.vti.testing.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class JWTAuthenFilter extends AbstractAuthenticationProcessingFilter  {

	@Autowired
	private UserDetails userDetails;
	protected JWTAuthenFilter(String url, AuthenticationManager authenManager) {
		super(url, authenManager);
		setAuthenticationManager(authenManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		// TODO Auto-generated method stub
		return getAuthenticationManager().authenticate(
				(Authentication) userDetails
				)
		;
	}
	



}
