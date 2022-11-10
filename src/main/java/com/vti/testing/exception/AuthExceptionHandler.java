package com.vti.testing.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class AuthExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

	// 403
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		String status = "403 - Forbidden";
		String message = "Can't access to this API";
		
		ExceptionResponse e = new ExceptionResponse(status, message);
		
		// convert ExceptionResponse to JSON
		com.fasterxml.jackson.databind.ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(e);
		
		// return json
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(json);
		
	}
	
	// 401
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		String status = "401 - Unauthorized";
		String message = "User Name or PassWord is incorrect";
			
		ExceptionResponse e = new ExceptionResponse(status, message);
		
		// convert ExceptionResponse to JSON
		com.fasterxml.jackson.databind.ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(e);
		
		// return json
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(json);
	}

}
