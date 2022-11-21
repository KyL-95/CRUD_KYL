package com.vti.testing.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.vti.testing.entity.User;
import com.vti.testing.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vti.testing.dto.UserDTO;
import com.vti.testing.responseobj.ResponseObj;
import com.vti.testing.service.interfaces.IUserService;

@RestController
public class AuthController {
    private final FacebookConnectionFactory factory = new FacebookConnectionFactory(
            "1135167277122964","97446104bfe11a6d0cd28c936f20ab99");
    @Autowired
    private AuthService authService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @PostMapping("/login")
    public String login(Model model){
        return "login";
    }
    @GetMapping("/login-abc")
    public String loginFacebook(){
        OAuth2Operations operations = factory.getOAuthOperations();
        OAuth2Parameters param = new OAuth2Parameters();
        param.setRedirectUri("http://localhost:8081/callback");
        param.setScope("email,public_profile");
        return operations.buildAuthenticateUrl(param);
    }
    @GetMapping("/callback")
    public String callbackLogin(@RequestParam("code") String authorCode, HttpServletResponse response) throws JsonProcessingException {
        OAuth2Operations operations = factory.getOAuthOperations();
        //get access infor (token, expiretime ...)
        String REDIRECT_URI = "http://localhost:8081/callback";
        AccessGrant accessGrant = operations.exchangeForAccess(authorCode, REDIRECT_URI,null);
//        Connection<Facebook> connection = factory.createConnection(accessGrant);
//        Facebook fb = connection.getApi();
//        String [] fields = {"id", "email","name"};
//        User user = fb.fetchObject("me",User.class,fields);
        return "AccessToken:     " + accessGrant.getAccessToken();
    }

    // get email from profile
    @GetMapping("/facebook-profile")
        public UserDTO getProfile(@RequestParam("email") String email){
        int a = email.indexOf("@");
        User loggingUser = authService.getUserByName(email.substring(0,a));
        UserDTO loggingDTO = modelMapper.map(loggingUser, UserDTO.class);
        return loggingDTO;
    }

    @GetMapping("/user/logining-user")	// Lấy thông tin user đang login : Dùng principal
//	@PreAuthorize("hasAnyRole('ADMIN')")  // Admin mới đc gọi API này
    public UserDTO loginInfor(Principal principal) {

        String loginUserName = principal.getName();
        System.out.println("Logining : " + loginUserName);
        com.vti.testing.entity.User entity = userService.getByUserName(loginUserName);
        System.out.println(loginUserName);
        return modelMapper.map(entity, UserDTO.class);

    }
}
