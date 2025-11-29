package com.airguardnet.user.infrastructure.persistence;

import com.airguardnet.user.domain.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "users")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String passwordHash;
    private String role;
    private String status;
    private Long planId;
    private Instant lastLoginAt;
    private int failedLoginCount;
    private Instant lockedUntil;
    private Instant createdAt;
    private Instant updatedAt;

    public static UserEntity fromDomain(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setLastName(user.getLastName());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setRole(user.getRole());
        entity.setStatus(user.getStatus());
        entity.setPlanId(user.getPlanId());
        entity.setLastLoginAt(user.getLastLoginAt());
        entity.setFailedLoginCount(user.getFailedLoginCount());
        entity.setLockedUntil(user.getLockedUntil());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        return entity;
    }

    public User toDomain() {
        return User.builder()
                .id(id)
                .name(name)
                .lastName(lastName)
                .email(email)
                .passwordHash(passwordHash)
                .role(role)
                .status(status)
                .planId(planId)
                .lastLoginAt(lastLoginAt)
                .failedLoginCount(failedLoginCount)
                .lockedUntil(lockedUntil)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
