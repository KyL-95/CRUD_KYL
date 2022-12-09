package com.vti.testing.login;

import com.vti.testing.exception.AuthExceptionHandler;
import com.vti.testing.filter.CustomAuthorFilter;
import com.vti.testing.jwt.JwtTokenProvider;
import com.vti.testing.service.UserDetailsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled	= true) // bỏ đi bị lỗi
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
//            .authenticationEntryPoint(authExceptionHandler)
            .accessDeniedHandler(authExceptionHandler)
            .and()
            .authorizeRequests()
                .antMatchers("/user/newUser", "/user/getAllActiveUser","/roles/getAll","/user/logging-user","/user/refresh-token","/login-abc").permitAll()
                .antMatchers("/user/getAll").hasAnyRole("ADMIN","MANAGER")
                .antMatchers("/user/delete/**").hasAnyRole("ADMIN")
                .antMatchers("/api/v1/product/getAll").hasAnyRole("MANAGER")
                .anyRequest().authenticated()
//            .and().oauth2Login();
            .and()
//            .addFilter(new CustomAuthenFilter(authenticationManagerBean(), jwtTokenProvider))
            .addFilterAfter(new CustomAuthorFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

    }
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

}
