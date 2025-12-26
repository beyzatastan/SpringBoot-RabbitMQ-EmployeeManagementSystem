package com.beyzatastan.auth_service.service;

import com.beyzatastan.auth_service.entity.User;

public interface JwtService {
    String generateAccessToken(User user);
    boolean isTokenValid(String token);
}
