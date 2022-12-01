package com.vti.testing.jwt;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String accessToken;
    private String refreshToken ;
    private final String tokenType = "Bearer";
    private String userName;
    private List<String> roles;


    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public JwtResponse(String accessToken, String refreshToken, String userName,  List<String> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userName = userName;
        this.roles = roles;
    }
}
