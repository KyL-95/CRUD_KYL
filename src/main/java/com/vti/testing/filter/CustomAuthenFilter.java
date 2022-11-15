package com.vti.testing.filter;

import com.vti.testing.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private static final Logger log = LoggerFactory.getLogger(CustomAuthenFilter.class);
    private final AuthenticationManager manager;

    public CustomAuthenFilter(AuthenticationManager manager) {
        this.manager = manager;
        setFilterProcessesUrl("/login-abc");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        log.info("User : ");
        String userName = request.getParameter("userName");
        String passWord = request.getParameter("passWord");
        log.info("User : " + userName);
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(userName, passWord);
        return manager.authenticate(userToken);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        new JwtTokenProvider().generateTokenForClient(response, (UserDetails) authentication.getPrincipal());

    }
}

	
