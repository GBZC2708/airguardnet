package com.airguardnet.device.application.usecase;

import com.airguardnet.device.infrastructure.persistence.UsageReportEntity;
import com.airguardnet.device.infrastructure.persistence.UsageReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListUsageReportsUseCase {

    private final UsageReportJpaRepository usageReportJpaRepository;

    public List<UsageReportEntity> execute() {
        return usageReportJpaRepository.findAllByOrderByGeneratedAtDesc();
    }
}
