package com.vti.testing.controller.login;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vti.testing.login.JwtTokenProvider;
import com.vti.testing.service.CustomUserDetails;

@RestController
public class LoginApi {
	@Autowired
	AuthenticationManager authenticationManager;

//	@PostMapping("/user-login/")
//	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
//	public String login(@RequestBody LoginInfor loginInfor) throws IOException {
		// Authen thông tin từ request
//		Authentication authentication = authenticationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(loginInfor.getUserName(),
//						loginInfor.getPassWord()));
//		// Nếu không xảy ra exception tức là thông tin hợp lệ
//		// Set thông tin authentication vào Security Context
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//		// Trả về jwt cho người dùng.
//		String JWT = JwtTokenProvider.generateTokenForClient((CustomUserDetails) authentication.getPrincipal());
//		System.out.println(JWT);
//		return "JWT: " + JWT;
//	}
}
