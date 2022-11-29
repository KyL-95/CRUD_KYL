package com.vti.testing.service.interfaces;

import com.vti.testing.entity.RefreshToken;

import java.util.Optional;

public interface IRefreshTokenService {
    public Optional<RefreshToken> findByToken(String token) ;
    public RefreshToken createRefreshToken(String userName);
    public RefreshToken verifyExpiration(RefreshToken token);
    public int deleteByUserId(int userId);

}
