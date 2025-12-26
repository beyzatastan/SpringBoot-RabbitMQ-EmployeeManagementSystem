package com.beyzatastan.auth_service.service;

import com.beyzatastan.auth_service.dto.EmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name:mailExchange}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key:mail.routing.key}")
    private String routingKey;


    public void sendEmail(EmailMessage message) {
        try {
            log.info("Sending email to queue: to={}, subject={}",
                    message.getTo(), message.getSubject());

            rabbitTemplate.convertAndSend(exchangeName, routingKey, message);

        } catch (Exception e) {
            log.error("Failed to send email to queue: {}", e.getMessage());
        }
    }

    public void sendWelcomeEmail(String email, String username) {
        EmailMessage message = EmailMessage.builder()
                .to(email)
                .subject("Welcome to HRMS! 🎉")
                .body(String.format(
                        "Hello %s,\n\n" +
                                "Welcome to our HR Management System!\n\n" +
                                "Your account has been successfully created.\n" +
                                "You can now login with your credentials.\n\n" +
                                "Best regards,\n" +
                                "HRMS Team",
                        username
                ))
                .type("WELCOME")
                .build();

        sendEmail(message);
    }

    public void sendPasswordResetEmail(String email, String resetToken) {
        EmailMessage message = EmailMessage.builder()
                .to(email)
                .subject("Password Reset Request 🔐")
                .body(String.format(
                        "Hello,\n\n" +
                                "You have requested to reset your password.\n\n" +
                                "Your reset token is: %s\n\n" +
                                "⏰ This token will expire in 15 minutes.\n\n" +
                                "If you didn't request this, please ignore this email.\n\n" +
                                "Best regards,\n" +
                                "HRMS Team",
                        resetToken
                ))
                .type("RESET_PASSWORD")
                .build();

        sendEmail(message);
    }

    public void sendPasswordChangedEmail(String email, String username) {
        EmailMessage message = EmailMessage.builder()
                .to(email)
                .subject("Password Changed Successfully ✅")
                .body(String.format(
                        "Hello %s,\n\n" +
                                "Your password has been changed successfully.\n\n" +
                                "If you didn't make this change, please contact us immediately.\n\n" +
                                "Best regards,\n" +
                                "HRMS Team",
                        username
                ))
                .type("PASSWORD_CHANGED")
                .build();

        sendEmail(message);
    }
}