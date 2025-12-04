package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.model.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserQueryService {

    public List<User> findByPlanName(List<User> users, String plan) {
        return users.stream()
                .filter(u -> u.getPlan() != null && Objects.equals(plan, u.getPlan().getName()))
                .collect(Collectors.toList());
    }
}
