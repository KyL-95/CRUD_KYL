package com.vti.testing.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.vti.testing.controller.loginform.LoginInfo;
import com.vti.testing.controller.loginform.TokenInfo;
import com.vti.testing.dto.UserDTO;
import com.vti.testing.entity.Role;
import com.vti.testing.entity.User;
import com.vti.testing.login.JwtTokenProvider;
import com.vti.testing.responseobj.ResponseObj;
import com.vti.testing.service.IUserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@RestController
public class AuthController {
    private static String JWT_SECRET = "kyl2803" ;
    private static final long JWT_EXPIRATION = 1000 * 15;
    @Autowired
    private IUserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/login-abc")
    public void login(@RequestBody LoginInfo infor) throws IOException {
    }
    @GetMapping("/logining-user")	// Lấy thông tin user đang login : Dùng principal
//	@PreAuthorize("hasAnyRole('ADMIN')")  // Admin mới đc gọi API này
    public UserDTO loginInfor(Principal principal) {
        String loginUserName = principal.getName();
        System.out.println("Logining : " + loginUserName);
        User entity = userService.getByUserName(loginUserName);
        System.out.println(loginUserName);
        return modelMapper.map(entity, UserDTO.class);

    }

    @GetMapping(value = "/getRefreshToken")
    public void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String authorHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorHeader != null && authorHeader.startsWith("Bearer ")) {
            try {
                String token = authorHeader.substring("Bearer ".length());
                String userName = JwtTokenProvider.getUserNameByJWT(token);
                // get user by this userName
                User user = userService.getByUserName(userName);
                List<String> roles = user.getRoles().stream()
                        .map(Role::getRoleName).collect(Collectors.toList());

//                String roles = Jwts.parser().setSigningKey(JWT_SECRET)
//                        .parseClaimsJws(token).getBody().get("Roles").toString();
                String refreshToken = Jwts.builder()
                        .claim("Roles" , roles)
                        .setSubject(userName)
                        .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                        .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                        .compact();
                response.getWriter().write(refreshToken);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (JWTVerificationException e) {
                e.printStackTrace();
            }
        }
//        else {
//            throw new RuntimeException("Refresh token is missing");
//        }

    }
}
