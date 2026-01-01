package com.beyzatastan.auth_service.service.impl;

import com.beyzatastan.auth_service.entity.User;
import com.beyzatastan.auth_service.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;


    @Override
    public String generateAccessToken(User user) {
        return "";
    }

    @Override
    public boolean isTokenValid(String token) {
        return false;
    }
}