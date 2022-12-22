package com.vti.testing.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

// AuthenticationEntryPoint : validate authentication
@Component
public class AuthExceptionHandler implements AccessDeniedHandler,AuthenticationEntryPoint {

	// 403
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
					   AccessDeniedException accessDeniedException) throws IOException, ServletException {
		String status = "403 - Forbidden";
		String message = "User can't access to this API";

		ExceptionResponse e = new ExceptionResponse(status, message);
		// return json
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(convertObjectToJson(e));

	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
						 AuthenticationException authException) throws IOException, ServletException {

		String status = "Error";
		String message = "Unauthorized";
		ExceptionResponse e = new ExceptionResponse(status, message);
		// return json
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(convertObjectToJson(e));

	}
	// convert ExceptionResponse to JSON
	private String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return " ";
		}
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(object);
		return json;
	}
}
