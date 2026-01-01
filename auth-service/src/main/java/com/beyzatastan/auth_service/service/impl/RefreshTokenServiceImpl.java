package com.beyzatastan.auth_service.service.impl;

import com.beyzatastan.auth_service.entity.RefreshToken;
import com.beyzatastan.auth_service.entity.User;
import com.beyzatastan.auth_service.repository.RefreshTokenRepository;
import com.beyzatastan.auth_service.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenExpiration;

    @Override
    @Transactional
    public RefreshToken create(User user) {
        // eski token'ları sil
        revokeAll(user);

        // yeni token oluştur
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiresAt(Instant.now().plusMillis(refreshTokenExpiration))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshToken verify(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.isExpired()) {
            throw new RuntimeException("Refresh token has expired. Please login again.");
        }
        return refreshToken;
    }

    @Override
    @Transactional
    public void revokeAll(User user) {
        refreshTokenRepository.deleteAllByUser(user);
    }
}