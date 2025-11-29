package com.airguardnet.user.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class User {
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
}
