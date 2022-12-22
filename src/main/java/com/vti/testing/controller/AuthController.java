package com.vti.testing.controller;

import com.vti.testing.dto.UserDTO;
import com.vti.testing.entity.RefreshToken;
import com.vti.testing.entity.User;
import com.vti.testing.exception.custom_exception.PassWordUncorrectedEx;
import com.vti.testing.exception.custom_exception.TokenRefreshException;
import com.vti.testing.jwt.*;
import com.vti.testing.login.LoginInfo;
import com.vti.testing.repository.IUserRepository;
import com.vti.testing.service.UserDetailsResult;
import com.vti.testing.service.interfaces.IRefreshTokenService;
import com.vti.testing.service.interfaces.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
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
	private JwtProperties properties;
    
    @PostMapping("/authenticate")
    public JwtResponse login(@RequestBody LoginInfo loginInfo) throws IOException {
        String userName = loginInfo.getUserName();
        UserDetails details = detailsService.loadUserByUsername(userName);
        if(detailsService.checkPassWord(userName,loginInfo.getPassWord())){
           return  jwtTokenProvider.generateTokenForClient(details);
        } else{
            throw new PassWordUncorrectedEx("Pass word has uncorrected!");
        }
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
            UserDetails userDetails = detailsService.loadUserByUsername(user.getUserName());
            String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
            return new TokenRefreshResponse(accessToken, refreshTokenFromRequest);
        }
        throw new TokenRefreshException("This refresh-token is not in database or this user has been login in other place");
    }

}
