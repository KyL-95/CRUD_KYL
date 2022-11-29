package com.vti.testing.jwt;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String accessToken;
    private String refreshToken ;
    private final String tokenType = "Bearer";
    private String username;
    private List<String> roles;


    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public JwtResponse(String accessToken, String refreshToken, String username,  List<String> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
        this.roles = roles;
    }
}
