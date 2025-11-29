package com.airguardnet.user.application.usecase;

import com.airguardnet.user.domain.model.ConfigParameter;
import com.airguardnet.user.domain.repository.ConfigParameterRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UpsertConfigParameterUseCase {

    private final ConfigParameterRepositoryPort configParameterRepositoryPort;

    public ConfigParameter execute(String key, String value) {
        Instant now = Instant.now();

        ConfigParameter parameter = configParameterRepositoryPort.findByKey(key)
                .map(existing -> {
                    existing.setValue(value);
                    existing.setUpdatedAt(now);
                    return existing;
                })
                .orElseGet(() -> ConfigParameter.builder()
                        .key(key)
                        .value(value)
                        .createdAt(now)
                        .updatedAt(now)
                        .build());

        if (parameter.getCreatedAt() == null) {
            parameter.setCreatedAt(now);
        }

        return configParameterRepositoryPort.save(parameter);
    }
}
