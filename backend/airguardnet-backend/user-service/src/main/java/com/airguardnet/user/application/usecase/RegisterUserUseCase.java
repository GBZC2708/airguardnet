package com.airguardnet.user.application.usecase;

import com.airguardnet.common.exception.ValidationException;
import com.airguardnet.user.domain.model.User;
import com.airguardnet.user.domain.repository.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public User register(String name, String lastName, String email, String rawPassword, String role, Long planId) {
        userRepositoryPort.findByEmail(email).ifPresent(u -> {
            throw new ValidationException("Email already registered");
        });
        if (!isPasswordValid(rawPassword)) {
            throw new ValidationException("Password does not meet complexity requirements");
        }
        User user = User.builder()
                .name(name)
                .lastName(lastName)
                .email(email)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .role(role)
                .status("ACTIVE")
                .planId(planId)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return userRepositoryPort.save(user);
    }

    private boolean isPasswordValid(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        return hasUpper && hasLower && hasDigit;
    }
}
