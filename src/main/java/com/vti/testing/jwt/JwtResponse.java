package com.vti.testing.jwt;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String accessToken;
    private String refreshToken ;
    private final String tokenType = "Bearer";
    private String userName;
    private Collection<? extends GrantedAuthority> roles;

    public JwtResponse(String accessToken, String refreshToken, String userName,  Collection<? extends GrantedAuthority> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userName = userName;
        this.roles = roles;
    }
}
