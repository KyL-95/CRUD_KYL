package com.vti.testing.controller;

import com.vti.testing.entity.RefreshToken;
import com.vti.testing.jwt.TokenRefreshRequest;
import com.vti.testing.jwt.TokenRefreshResponse;
import com.vti.testing.login.LoginInfo;
import com.vti.testing.dto.UserDTO;
import com.vti.testing.entity.Role;
import com.vti.testing.entity.User;
import com.vti.testing.jwt.JwtTokenProvider;
import com.vti.testing.repository.IUserRepository;
import com.vti.testing.responseobj.ResponseObj;
import com.vti.testing.service.interfaces.IRefreshTokenService;
import com.vti.testing.service.interfaces.IUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@RestController
public class AuthController {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private IRefreshTokenService refreshTokenService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @GetMapping("/user/logging-user")	// Lấy thông tin user đang login : Dùng principal
//	@PreAuthorize("hasAnyRole('ADMIN')")  // Admin mới đc gọi API này
    public UserDTO loginInfo(Principal principal) {
//        String loginUserName = authentication.getName();
        String loginUserName = principal.getName();
        System.out.println("Logging : " + loginUserName);
        User entity = userService.getByUserName(loginUserName);
        System.out.println(loginUserName);
        return modelMapper.map(entity, UserDTO.class);

    }

    @PostMapping("/user/refresh-token")
    public TokenRefreshResponse refreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest){
        String refreshTokenFromRequest = tokenRefreshRequest.getRefreshToken();
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenFromRequest).get();
//        refreshToken = refreshTokenService.verifyExpiration(refreshToken);
        // Get user from refreshToken
        User user = userRepository.findById(refreshToken.getUser().getUserId()).get();
        String accessToken = jwtTokenProvider.generateJwt(user.getUserName());
        return new TokenRefreshResponse(accessToken,refreshTokenFromRequest);
    }
}
