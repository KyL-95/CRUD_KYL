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

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService implements IRefreshTokenService {
    @Autowired
    private  IRefreshTokenRepository refreshTokenRepository;
    @Autowired
    private  IUserRepository userRepository; // bị null ????
    @Value("${jwt.JWT_REFRESH_EXPIRATION}")
    private Long refreshTokenDurationMs ; // 1 hour
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(String userName) {
        User user = userRepository.findByUserName(userName);
        RefreshToken tokenDel = user.getRefreshToken();
        if(tokenDel != null ) {
            tokenDel.setToken(UUID.randomUUID().toString());
            tokenDel.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshTokenRepository.save(tokenDel);
            return tokenDel;
        }else{
            RefreshToken refreshToken = new RefreshToken();

            refreshToken.setUser(user);
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setToken(UUID.randomUUID().toString());

            refreshToken = refreshTokenRepository.save(refreshToken);
            return refreshToken;
        }
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()  ) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken() + "...Refresh token was expired. Please make a new signin request");
        }


        return token;
    }



    @Override
    @Transactional
    public int deleteByUserName(String userName) {
         return refreshTokenRepository.deleteByUser(userRepository.findByUserName(userName));

    }
}
