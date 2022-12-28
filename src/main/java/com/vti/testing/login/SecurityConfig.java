package com.vti.testing.login;

import com.vti.testing.exception.AuthExceptionHandler;
import com.vti.testing.filter.JwtFilter;
import com.vti.testing.filter.TokenFilter;
import com.vti.testing.jwt.JwtTokenProvider;
import com.vti.testing.service.UserDetailsResult;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled	= true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private TokenFilter tokenFilter;
    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserDetailsResult details;
    @Autowired
    private AuthExceptionHandler authExceptionHandler;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(details).passwordEncoder(new BCryptPasswordEncoder());
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    http
            .csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint(authExceptionHandler)
            .accessDeniedHandler(authExceptionHandler)
            .and()
            .authorizeRequests()
                .antMatchers("/roles/getAll","/authenticate","/api/v1/product/getAll","/send-email").permitAll()
                .antMatchers("/user/newUser","/user/refresh-token","/api/v1/product/findAll").permitAll()
                .antMatchers("/user/delete/**","/user/getAll","/user/findBy","/user/getByUserName").permitAll()
//                .antMatchers("/user/getAll").hasAnyRole("ADMIN","MANAGER")
//                .antMatchers("/user/delete/**").hasAnyRole("ADMIN")
                .antMatchers("/user/getNoActiveUser/**").hasAnyRole("ADMIN")
//                .antMatchers("/api/v1/product/getAll").hasAnyRole("MANAGER")
                .anyRequest().authenticated()
            .and().formLogin()
            .and().logout()
//            .and().oauth2Login();
            .and()
//            .addFilter(new CustomAuthenFilter(authenticationManagerBean(), jwtTokenProvider))
            .addFilterBefore(jwtFilter, BasicAuthenticationFilter.class);

    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



}
