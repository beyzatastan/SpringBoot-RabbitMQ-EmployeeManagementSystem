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

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole("EMPLOYEE");
        user.setIsActive(true);

        User savedUser = userRepository.save(user);
        log.info("User saved to database: {}", savedUser.getId());

        String accessToken = jwtService.generateAccessToken(savedUser);
        RefreshToken refreshToken = refreshTokenService.create(savedUser);

        // hoşgeldin maili gönder rabbtmq ile
        emailProducer.sendWelcomeEmail(savedUser.getEmail(), savedUser.getUsername());
        log.info("Welcome email sended");

        return String.format(accessToken, refreshToken.getToken(), savedUser.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public String signin(String usernameOrEmail, String rawPassword) {

        User user = userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new RuntimeException("Invalid credentials")));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.getIsActive()) {
            log.warn("Account deactivated: {}", usernameOrEmail);
            throw new RuntimeException("Account is deactivated");
        }

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.create(user);

        return String.format(accessToken, refreshToken.getToken(),
                user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    @Override
    public String refreshAccessToken(String refreshToken) {

        RefreshToken verified = refreshTokenService.verify(refreshToken);
        User user = verified.getUser();

        String newAccessToken = jwtService.generateAccessToken(user);
        log.info("Access token refreshed for user: {}", user.getUsername());

        return newAccessToken;
    }

    @Override
    @Transactional
    public void requestPasswordReset(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No account found for this email"));

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

        // şifre sıfırlama maili gönderildi
        emailProducer.sendPasswordResetEmail(email, token);
        log.info("Password reset email sended");
    }

    @Override
    @Transactional
    public void resetPassword(String resetToken, String newPassword) {

        PasswordResetToken token = resetTokenRepository.findByToken(resetToken)
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        if (token.getUsed()) {
            throw new RuntimeException("This reset token has already been used");
        }

        if (token.isExpired()) {
            throw new RuntimeException("Reset token has expired. Please request a new one.");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info(" Password updated for user: {}", user.getUsername());

        token.setUsed(true);
        resetTokenRepository.save(token);

        emailProducer.sendPasswordChangedEmail(user.getEmail(), user.getUsername());
        log.info("Password changed email sended");

        log.info("Password reset completed for user: {}", user.getUsername());
    }
}