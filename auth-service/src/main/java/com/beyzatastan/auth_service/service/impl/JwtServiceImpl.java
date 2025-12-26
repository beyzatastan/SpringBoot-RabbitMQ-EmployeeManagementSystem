package com.beyzatastan.auth_service.service.impl;

import com.beyzatastan.auth_service.entity.User;
import com.beyzatastan.auth_service.service.JwtService;
import com.beyzatastan.auth_service.service.RefreshTokenService;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {
    @Override
    public String generateAccessToken(User user) {
        return "";
    }

    @Override
    public String generateRefreshToken(User user) {
        return "";
    }

    @Override
    public String extractUsername(String token) {
        return "";
    }

    @Override
    public boolean isTokenValid(String token) {
        return false;
    }
}
