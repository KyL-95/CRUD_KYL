//package com.vti.testing.filter;
//
//import com.vti.testing.jwt.JwtTokenProvider;
//import lombok.AllArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@AllArgsConstructor
//public class CustomAuthenFilter extends UsernamePasswordAuthenticationFilter {
//    private JwtTokenProvider jwtTokenProvider;
//    private final AuthenticationManager manager;
//
//    public CustomAuthenFilter(AuthenticationManager manager
//            , JwtTokenProvider jwtTokenProvider
//    ) {
//        this.manager = manager;
//        this.jwtTokenProvider = jwtTokenProvider;
//        setFilterProcessesUrl("/login-abc");
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//           String userName = request.getParameter("userName");
//           String passWord = request.getParameter("passWord");
//           UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(userName,passWord);
//           return manager.authenticate(userToken);
//
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
//            jwtTokenProvider.generateTokenForClient( authentication.getName());
//            chain.doFilter(request,response);
//    }
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User name or password is incorrect!");
//    }
//}
//
//
