package com.beyzatastan.auth_service.service.impl;

import com.beyzatastan.auth_service.entity.PasswordResetToken;
import com.beyzatastan.auth_service.entity.RefreshToken;
import com.beyzatastan.auth_service.entity.User;
import com.beyzatastan.auth_service.repository.PasswordResetTokenRepository;
import com.beyzatastan.auth_service.repository.UserRepository;
import com.beyzatastan.auth_service.service.AuthService;
import com.beyzatastan.auth_service.service.EmailProducer;
import com.beyzatastan.auth_service.service.JwtService;
import com.beyzatastan.auth_service.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final EmailProducer emailProducer;

    @Override
    @Transactional
    public String signup(String username, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            log.warn("Email already exists: {}", email);
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole("EMPLOYEE");
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        // JWT tokens oluştur
        String accessToken = jwtService.generateAccessToken(savedUser);
        RefreshToken refreshToken = refreshTokenService.create(savedUser);
        log.info("JWT tokens generated for user: {}", savedUser.getUsername());

        try {
            emailProducer.sendWelcomeEmail(savedUser.getEmail(), savedUser.getUsername());
        } catch (Exception e) {
            log.error("Failed to queue welcome email: {}", e.getMessage());
        }
        return String.format("%s|%s|%d",
                accessToken,
                refreshToken.getToken(),
                savedUser.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public String signin(String email, String rawPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    return new RuntimeException("Invalid username or password");
                });

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("Your account has been deactivated. Please contact support.");
        }

        log.info("User authenticated: {}", user.getUsername());

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.create(user);
        log.info("JWT tokens generated for user: {}", user.getUsername());

        return String.format("%s|%s|%d|%s|%s|%s",
                accessToken,
                refreshToken.getToken(),
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole());
    }

    @Override
    @Transactional(readOnly = true)
    public String refreshAccessToken(String refreshToken) {
       //refresh tokenı doğruluyorm
        RefreshToken verified = refreshTokenService.verify(refreshToken);
        User user = verified.getUser();

        String newAccessToken = jwtService.generateAccessToken(user);
        return newAccessToken;
    }


    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    return new RuntimeException("No account found with this email address");
                });

        String token = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiresAt(Instant.now().plusSeconds(900))
                .used(false)
                .build();

        resetTokenRepository.save(resetToken);
        log.info("Password reset token created: {} (expires in 15 min)", token);

        // Reset email gönder rabbitMQ
        try {
            emailProducer.sendPasswordResetEmail(email, token);
            log.info("Password reset email queued for: {}", email);
        } catch (Exception e) {
            log.error("Failed to queue reset email: {}", e.getMessage());
            throw new RuntimeException("Failed to send password reset email. Please try again.");
        }
    }

    @Override
    @Transactional
    public void resetPassword(String resetToken, String newPassword) {
        PasswordResetToken token = resetTokenRepository.findByToken(resetToken)
                .orElseThrow(() -> {
                    log.warn("Invalid reset token: {}", resetToken);
                    return new RuntimeException("Invalid or expired reset token");
                });

        if (token.getUsed()) {
            log.warn("Reset token already used: {}", resetToken);
            throw new RuntimeException("This reset token has already been used");
        }

        if (token.isExpired()) {
            log.warn("Reset token expired: {}", resetToken);
            throw new RuntimeException("Reset token has expired. Please request a new one.");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info(" Password updated for user: {}", user.getUsername());

        token.setUsed(true);
        resetTokenRepository.save(token);
        log.info("Reset token marked as used: {}", resetToken);

        // şifre değişti email gönder
        try {
            emailProducer.sendPasswordChangedEmail(user.getEmail(), user.getUsername());
            log.info(" Password changed email queued for: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to queue password changed email: {}", e.getMessage());
        }

    }
}