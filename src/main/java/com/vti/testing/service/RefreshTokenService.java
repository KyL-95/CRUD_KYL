package com.vti.testing.service;

import com.vti.testing.entity.RefreshToken;
import com.vti.testing.entity.User;
import com.vti.testing.exception.custom_exception.TokenRefreshException;
import com.vti.testing.repository.IRefreshTokenRepository;
import com.vti.testing.repository.IUserRepository;
import com.vti.testing.service.interfaces.IRefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService implements IRefreshTokenService {
    @Autowired
    private  IRefreshTokenRepository refreshTokenRepository;
    @Autowired
    private  IUserRepository userRepository;
    @Value("${jwt.refreshExpiration}")
    private Long refreshTokenDurationMs ;
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(String userName) {
        User user = userRepository.findByUserName(userName);
        RefreshToken tokenUpdate = user.getRefreshToken();
        if(tokenUpdate != null ) {
            tokenUpdate.setToken(UUID.randomUUID().toString());
//            tokenUpdate.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            tokenUpdate.setExpiryDate(new Timestamp(System.currentTimeMillis() + refreshTokenDurationMs));
            refreshTokenRepository.save(tokenUpdate);
            return tokenUpdate;
        }else{
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setUser(user);
            refreshToken.setExpiryDate(new Timestamp(System.currentTimeMillis() + refreshTokenDurationMs));
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken = refreshTokenRepository.save(refreshToken);
            return refreshToken;
        }
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        Timestamp tokenExpiry = token.getExpiryDate();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (tokenExpiry.before(now)) {
//            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken() + " Refresh token has been expired. Please make a new sign-in request");
        }

        return token;
    }



}
