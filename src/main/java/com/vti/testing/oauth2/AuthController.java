package com.vti.testing.oauth2;
import javax.servlet.http.HttpServletResponse;

import com.vti.testing.dto.UserDTO;
import com.vti.testing.responseobj.ResponseObj;
import com.vti.testing.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.facebook.api.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.vti.testing.service.interfaces.IUserService;

import java.security.Principal;

@RestController
public class AuthController {
    private final FacebookConnectionFactory FACTORY = new FacebookConnectionFactory(
            "1135167277122964","97446104bfe11a6d0cd28c936f20ab99");
    private final OAuth2Operations OPERATIONS = FACTORY.getOAuthOperations();
    private final String REDIRECT_URI = "http://localhost:8081/callback";
    @Autowired
    private AuthService authService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RestFB restFb;
    @GetMapping("/login-abc")
    public String loginFacebook(){
        OAuth2Parameters param = new OAuth2Parameters();
        param.setRedirectUri(REDIRECT_URI);
        param.setScope("email,public_profile");
        String authenticated = OPERATIONS.buildAuthenticateUrl(param);
        return authenticated;
    }
    @GetMapping("/callback")
    public ResponseObj callbackLogin(@RequestParam("code") String authorCode, HttpServletResponse response) throws JsonProcessingException {
        System.out.println(authorCode);
        //get access info (token, expiratime ...)
        AccessGrant accessGrant = OPERATIONS.exchangeForAccess(authorCode, REDIRECT_URI,null);
        Connection<Facebook> connection = FACTORY.createConnection(accessGrant);
        Facebook fb = connection.getApi();
        String [] fields = {"id", "email","name"};
        User user = fb.fetchObject("me", User.class,fields); // fb user' info

        UserDetails userDetail = restFb.buildUser(user.getEmail());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
                userDetail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseObj("", "Logging user : ","UserName :" + authentication.getName());
    }

    // get email from profile
    @GetMapping("/facebook-profile")
        public ResponseObj getProfile(@RequestParam("email") String email){
        int a = email.indexOf("@");
        return authService.getUserByName(email.substring(0,a));
    }

    @GetMapping("/user/logining-user")	// Lấy thông tin user đang login : Dùng principal
//	@PreAuthorize("hasAnyRole('ADMIN')")  // Admin mới đc gọi API này
    public UserDTO loginInfor() {

        String loginUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Logining : " + loginUserName);
        com.vti.testing.entity.User entity = userService.getByUserName(loginUserName);
        System.out.println(loginUserName);
        return modelMapper.map(entity, UserDTO.class);

    }
}
