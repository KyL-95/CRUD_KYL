package com.vti.testing.filter;

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
public class TokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserDetailsService userDetailsService;
    private final Logger log = LoggerFactory.getLogger(TokenFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            SecurityContextHolder.clearContext();
        }
        if (authHeader == null || !authHeader.startsWith(jwtTokenProvider.getPREFIX_TOKEN())) {
            filterChain.doFilter(request,response);
            return;
        }
            try {
                 String token = authHeader.substring(jwtTokenProvider.getPREFIX_TOKEN().length());
                String userName = jwtTokenProvider.getUserNameByJWT(token);
                if (userName != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userName, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                }
            } catch (ExpiredJwtException e) {
                log.info("Token has been expired");
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Token has been expired");
            } catch (MalformedJwtException e) {
                log.info("Token has been invalid");
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Token has been invalid");
            }

    }

}
