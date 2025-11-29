package com.airguardnet.user.application.usecase;

import com.airguardnet.user.domain.model.VersionHistory;
import com.airguardnet.user.domain.repository.VersionHistoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListVersionHistoryUseCase {
    private final VersionHistoryRepositoryPort versionHistoryRepositoryPort;

    public List<VersionHistory> execute() {
        return versionHistoryRepositoryPort.findAll();
    }
}
