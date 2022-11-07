package com.vti.testing.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.vti.testing.exception.AuthExceptionHandler;
import com.vti.testing.filter.CustomAuthenFilter;
import com.vti.testing.filter.CustomAuthorFilter;
import com.vti.testing.service.UserDetailsResult;

@Configuration
@EnableWebSecurity
// Config Scope access in Class (@PreAuthorize)
@EnableGlobalMethodSecurity(
							jsr250Enabled 	= true,
							prePostEnabled	= true,
							securedEnabled 	= true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsResult userDetails;
	@Autowired
	private AuthExceptionHandler authExceptionHandler;
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetails) // Cung cấp userDetailsService cho spring security
		.passwordEncoder(new BCryptPasswordEncoder());  // Dùng BCrypt để encode passWord 
	}
	// Login 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Custom lại AIP login
		UsernamePasswordAuthenticationFilter authenticationFilter = 
				new CustomAuthenFilter(authenticationManagerBean());
		authenticationFilter.setFilterProcessesUrl("/login-abc");
		http
		.cors()
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.
		  STATELESS) .and() // Xử lý khi xuất hiện lỗi 401 / 403 
		  .exceptionHandling()
		// Lỗi 401
//		.authenticationEntryPoint(authExceptionHandler)
		// Lỗi 403
//		.accessDeniedHandler(authExceptionHandler)
		.and()
		.authorizeRequests()
			.antMatchers("/updatePassWord/**","/api/v1/product/getAll","/login-abc").permitAll()
//			.antMatchers("/getAll").hasAnyRole("MANAGER")
//			.anyRequest().authenticated()
			.and().csrf().disable();
//			.addFilter(new CustomAuthenFilter(authenticationManagerBean()))
//			.addFilterBefore(new CustomAuthorFilter(),UsernamePasswordAuthenticationFilter.class);
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
	  
}
