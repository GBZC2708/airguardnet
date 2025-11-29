package com.airguardnet.user.application.usecase;

import com.airguardnet.common.exception.UnauthorizedException;
import com.airguardnet.user.domain.model.AccessLog;
import com.airguardnet.user.domain.model.SystemLog;
import com.airguardnet.user.domain.model.User;
import com.airguardnet.user.domain.repository.AccessLogRepositoryPort;
import com.airguardnet.user.domain.repository.SystemLogRepositoryPort;
import com.airguardnet.user.domain.repository.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.airguardnet.common.exception.ValidationException;
import com.airguardnet.common.exception.NotFoundException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private static final int DEFAULT_LOCK_THRESHOLD = 5;
    private static final int DEFAULT_LOCK_MINUTES = 15;

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final AccessLogRepositoryPort accessLogRepositoryPort;
    private final SystemLogRepositoryPort systemLogRepositoryPort;

    public LoginResult login(String email, String password, String ipAddress) {
        User user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(Instant.now())) {
            throw new UnauthorizedException("Account locked until " + user.getLockedUntil());
        }

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            int failed = user.getFailedLoginCount() + 1;
            User updated = User.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .passwordHash(user.getPasswordHash())
                    .role(user.getRole())
                    .status(user.getStatus())
                    .planId(user.getPlanId())
                    .failedLoginCount(failed)
                    .lockedUntil(failed >= DEFAULT_LOCK_THRESHOLD
                            ? Instant.now().plus(DEFAULT_LOCK_MINUTES, ChronoUnit.MINUTES)
                            : null)
                    .createdAt(user.getCreatedAt())
                    .updatedAt(Instant.now())
                    .build();
            userRepositoryPort.save(updated);
            systemLogRepositoryPort.save(SystemLog.builder()
                    .type("WARNING")
                    .source("user-service")
                    .message("Failed login for " + email)
                    .createdAt(Instant.now())
                    .build());
            throw new UnauthorizedException("Invalid credentials");
        }

        User reset = User.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .role(user.getRole())
                .status(user.getStatus())
                .planId(user.getPlanId())
                .failedLoginCount(0)
                .lockedUntil(null)
                .lastLoginAt(Instant.now())
                .createdAt(user.getCreatedAt())
                .updatedAt(Instant.now())
                .build();
        userRepositoryPort.save(reset);
        accessLogRepositoryPort.save(AccessLog.builder()
                .userId(user.getId())
                .action("LOGIN")
                .ipAddress(ipAddress)
                .createdAt(Instant.now())
                .build());
        return new LoginResult(
                user.getId(),
                user.getRole(),
                user.getPlanId(),
                user.getStatus(),
                user.getEmail(),
                user.getName()
        );
    }
}
