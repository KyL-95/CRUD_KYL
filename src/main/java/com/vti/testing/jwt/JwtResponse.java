package com.vti.testing.jwt;
import lombok.*;
@Getter
@Setter
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private final String tokenType;

    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
    }
}
