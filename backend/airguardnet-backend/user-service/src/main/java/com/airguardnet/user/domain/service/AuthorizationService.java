package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.model.User;

public class AuthorizationService {

    public boolean canCreateUser(User actor) {
        return actor != null && "ADMIN".equals(actor.getRole());
    }

    public boolean canViewGlobalReports(User user) {
        return user != null && "ADMIN".equals(user.getRole());
    }
}
