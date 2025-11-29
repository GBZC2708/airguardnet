package com.airguardnet.user.infrastructure.web;

import com.airguardnet.user.domain.model.User;

public class UserDTO {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String role;
    private String status;
    private Long planId;

    public static UserDTO fromDomain(User user) {
        UserDTO dto = new UserDTO();
        dto.id = user.getId();
        dto.name = user.getName();
        dto.lastName = user.getLastName();
        dto.email = user.getEmail();
        dto.role = user.getRole();
        dto.status = user.getStatus();
        dto.planId = user.getPlanId();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public Long getPlanId() {
        return planId;
    }
}
