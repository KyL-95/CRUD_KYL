package com.vti.testing.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.vti.testing.exception.AuthExceptionHandler;
import com.vti.testing.service.UserDetails;

@Configuration
@EnableWebSecurity
// Config Scope access in Class (@PreAuthorize)
@EnableGlobalMethodSecurity(jsr250Enabled 	= true,
							prePostEnabled	= true,
							securedEnabled 	= true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetails userDetails;
	
	// Tạo authExceptionHandler để bắt exception
	@Autowired
	private AuthExceptionHandler authExceptionHandler;
	
	// Phân quyền
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetails) // Cung cấp userDetailsService cho spring security
		.passwordEncoder(new BCryptPasswordEncoder());  // Dùng BCrypt để encode passWord 
		
	}
	
	// Login
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// Khi không đủ quyền truy cập sẽ bị chuyển hướng
//        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/error");
		
		http.cors()
		.and()
		// Xử lý khi xuất hiện lỗi 401 / 403
		.exceptionHandling()
		// Lỗi 401
		.authenticationEntryPoint(authExceptionHandler) 
		// Lỗi 403
		.accessDeniedHandler(authExceptionHandler)
		.and()
		.authorizeRequests()
			// Cho phép truy cập ko cần login
//			.antMatchers("/newUser", "/updatePassWord/**", "/logining-user" ).permitAll()
			// Có quyền Manager sẽ được truy cập
//			.antMatchers("/getAll").hasAnyRole("MANAGER")
			// Mọi request còn lại đều phải cần login trước khi truy cập API
			.anyRequest().authenticated()
			// Cho phép logout 
			.and().logout().permitAll()
			.and().httpBasic().and().csrf().disable();
//			.addFilterBefore(new , null);
	}
	  
}
