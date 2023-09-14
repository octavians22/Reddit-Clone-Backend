package com.example.RedditClone.service;

import com.example.RedditClone.exceptions.SpringRedditException;
import com.example.RedditClone.model.RefreshToken;
import com.example.RedditClone.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(60*60*24*365));
        //expirare refresh token sau nu?
        return refreshTokenRepository.save(refreshToken);
    }

    void validateRefreshToken(String token){
        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new SpringRedditException("Invalid refresh token"));
        if(refreshTokenRepository.findByToken(token).get().getExpiryDate().isBefore(Instant.now())){
            throw new SpringRedditException("Refresh token expired");
        }
    }

    public void deleteRefreshToken(String token){
        refreshTokenRepository.findByToken(token)
                        .orElseThrow(() -> new SpringRedditException("Token already deleted"));
        refreshTokenRepository.deleteByToken(token);
    }
}
