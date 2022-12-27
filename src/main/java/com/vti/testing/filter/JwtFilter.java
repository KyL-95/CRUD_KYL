package com.vti.testing.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vti.testing.exception.ExceptionResponse;
import com.vti.testing.jwt.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
//@Profile("prod")
public class JwtFilter extends OncePerRequestFilter {
    private final Logger log = LoggerFactory.getLogger(com.vti.testing.filter.TokenFilter.class);
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            SecurityContextHolder.clearContext();
        }
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(jwtTokenProvider.getPREFIX_TOKEN())) {
                System.out.println("-----------------Don't need token Filter---------------");
                filterChain.doFilter(request, response);
                return;
        }
                try {
                    System.out.println("-------------Need token------------");
                    String token = authHeader.substring(jwtTokenProvider.getPREFIX_TOKEN().length());
                    String userName = jwtTokenProvider.getUserNameByJWT(token);
                        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userName, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        filterChain.doFilter(request, response);
                } catch (ExpiredJwtException e) {
                    log.error("Expired token");
                    response.setStatus(401);
                    ExceptionResponse ex = new ExceptionResponse("Token error", "Token has been expired");
                    response.getWriter().write(convertObjectToJson(ex));
                } catch (MalformedJwtException e) {
                    log.error("Invalid token");
                    response.setStatus(401);
                    ExceptionResponse ex = new ExceptionResponse("Token error", "Token has been invalid");
                    response.getWriter().write("Token has been invalid");
                }
        }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return " ";
        }
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }

}
