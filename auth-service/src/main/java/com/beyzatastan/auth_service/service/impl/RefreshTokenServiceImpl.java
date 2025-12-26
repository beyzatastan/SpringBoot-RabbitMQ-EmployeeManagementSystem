package com.beyzatastan.auth_service.service.impl;

import com.beyzatastan.auth_service.entity.RefreshToken;
import com.beyzatastan.auth_service.entity.User;
import com.beyzatastan.auth_service.service.RefreshTokenService;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Override
    public RefreshToken create(User user) {
        return null;
    }

    @Override
    public RefreshToken verify(String token) {
        return null;
    }

    @Override
    public void revokeAll(User user) {

    }
}
