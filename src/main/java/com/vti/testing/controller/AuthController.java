package com.vti.testing.controller;

import com.vti.testing.login.LoginInfo;
import com.vti.testing.dto.UserDTO;
import com.vti.testing.entity.Role;
import com.vti.testing.entity.User;
import com.vti.testing.jwt.JwtTokenProvider;
import com.vti.testing.service.interfaces.IUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@RestController
public class AuthController {
    @Value("${jwt.JWT_SECRET}")
    private String jwtSecret;

    private static final long JWT_EXPIRATION = 1000 * 600;
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider; // Bị null
    @Autowired
    private IUserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/login-abc")
    public void login() throws IOException {
    }
    @GetMapping("/user/logining-user")	// Lấy thông tin user đang login : Dùng principal
//	@PreAuthorize("hasAnyRole('ADMIN')")  // Admin mới đc gọi API này
    public UserDTO loginInfor(Principal principal) {
        String loginUserName = principal.getName();
        System.out.println("Logining : " + loginUserName);
        User entity = userService.getByUserName(loginUserName);
        System.out.println(loginUserName);
        return modelMapper.map(entity, UserDTO.class);

    }

//    @GetMapping(value = "/getRefreshToken")
//    public void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException{
//
//        String authorHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if(authorHeader != null && authorHeader.startsWith("Bearer ")) {
//            try {
//                String token = authorHeader.substring("Bearer ".length());
//                String userName = jwtTokenProvider.getUserNameByJWT(token);
//                // get user by this userName
//                User user = userService.getByUserName(userName);
//                List<String> roles = user.getRoles().stream()
//                        .map(Role::getRoleName).collect(Collectors.toList());
//
////                String roles = Jwts.parser().setSigningKey(JWT_SECRET)
////                        .parseClaimsJws(token).getBody().get("Roles").toString();
//                String refreshToken = Jwts.builder()
//                        .claim("Roles" , roles)
//                        .setSubject(userName)
//                        .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
//                        .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                        .compact();
//                response.getWriter().write(refreshToken);
//            } catch (IllegalArgumentException | JWTVerificationException e) {
//                e.printStackTrace();
//            }
//        }
//        else {
//            throw new RuntimeException("Refresh token is missing");
//        }
//
//    }
}
