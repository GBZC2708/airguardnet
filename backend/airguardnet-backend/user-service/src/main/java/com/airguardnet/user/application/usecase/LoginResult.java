package com.airguardnet.user.application.usecase;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResult {
    private Long userId;
    private String role;
    private Long planId;
    private String status;
    private String email;
    private String name;
}
