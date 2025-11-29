package com.airguardnet.user.application.usecase;

import com.airguardnet.user.domain.model.AccessLog;
import com.airguardnet.user.domain.repository.AccessLogRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListAccessLogsUseCase {

    private final AccessLogRepositoryPort accessLogRepositoryPort;

    public List<AccessLog> execute() {
        return accessLogRepositoryPort.findAllOrderedByCreatedAtDesc();
    }
}
