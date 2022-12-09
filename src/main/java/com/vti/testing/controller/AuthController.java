package com.vti.testing.controller;

import com.vti.testing.dto.UserDTO;
import com.vti.testing.entity.RefreshToken;
import com.vti.testing.entity.User;
import com.vti.testing.exception.custom_exception.PassWordUncorrectedEx;
import com.vti.testing.exception.custom_exception.TokenRefreshException;
import com.vti.testing.jwt.JwtTokenProvider;
import com.vti.testing.jwt.TokenRefreshRequest;
import com.vti.testing.jwt.TokenRefreshResponse;
import com.vti.testing.login.LoginInfo;
import com.vti.testing.repository.IUserRepository;
import com.vti.testing.service.UserDetailsResult;
import com.vti.testing.service.interfaces.IRefreshTokenService;
import com.vti.testing.service.interfaces.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@RestController
public class AuthController {
    @Autowired
    private UserDetailsResult detailsService;
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

    @PostMapping("/login-abc")
    public void login(@RequestBody LoginInfo loginInfo, HttpServletResponse response) throws IOException {
        String userName = loginInfo.getUserName();
       final UserDetails details = detailsService.loadUserByUsername(userName);
        if(detailsService.checkPassWord(userName,loginInfo.getPassWord())){
            jwtTokenProvider.generateTokenForClient(response,userName);
        } else{
            throw new PassWordUncorrectedEx("Pass word has uncorrected!");
        }
    }
    @GetMapping("/user/logging-user")
//	@PreAuthorize("hasAnyRole('ADMIN')")  // Admin mới đc gọi API này
    public UserDTO loginInfo(Principal principal) {
        String loginUserName = principal.getName();
        System.out.println("Logining in : " + loginUserName);
        User entity = userService.getByUserName(loginUserName);
        return modelMapper.map(entity, UserDTO.class);
    }
    @PostMapping("/user/refresh-token")
    public TokenRefreshResponse refreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest){
        String refreshTokenFromRequest = tokenRefreshRequest.getRefreshToken();
        Optional<RefreshToken> refreshTokenOptional = refreshTokenService.findByToken(refreshTokenFromRequest);
        if(refreshTokenOptional.isPresent()) {
            RefreshToken refreshToken = refreshTokenOptional.get();
            refreshToken = refreshTokenService.verifyExpiration(refreshToken);
            // Get user from refreshToken
            User user = userRepository.findById(refreshToken.getUser().getUserId()).get();

            String accessToken = jwtTokenProvider.generateAccessToken(user.getUserName());
            return new TokenRefreshResponse(accessToken, refreshTokenFromRequest);
        }
        throw new TokenRefreshException("This token is not in database");
    }
}
