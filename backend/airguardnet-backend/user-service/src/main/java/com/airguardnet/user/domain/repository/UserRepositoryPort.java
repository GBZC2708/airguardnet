package com.airguardnet.user.domain.repository;

import com.airguardnet.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findById(Long id);

    List<User> findAll();
}
