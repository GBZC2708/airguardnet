package com.airguardnet.user.application.usecase;

import com.airguardnet.user.domain.model.User;
import com.airguardnet.user.domain.repository.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListUsersUseCase {
    private final UserRepositoryPort userRepositoryPort;

    public List<User> listAll() {
        return userRepositoryPort.findAll();
    }
}
