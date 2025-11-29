package com.airguardnet.user.application.usecase;

import com.airguardnet.user.domain.model.SystemLog;
import com.airguardnet.user.domain.repository.SystemLogRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListSystemLogsUseCase {

    private final SystemLogRepositoryPort systemLogRepositoryPort;

    public List<SystemLog> execute() {
        return systemLogRepositoryPort.findAllOrderedByCreatedAtDesc();
    }
}
