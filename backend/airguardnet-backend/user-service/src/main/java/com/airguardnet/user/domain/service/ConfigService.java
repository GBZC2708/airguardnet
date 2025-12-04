package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.model.ConfigChangeLog;
import com.airguardnet.user.domain.model.ConfigParameter;
import com.airguardnet.user.domain.model.User;
import com.airguardnet.user.domain.repository.ConfigChangeLogRepositoryPort;
import com.airguardnet.user.domain.repository.ConfigParameterRepositoryPort;

import java.time.Instant;

public class ConfigService {

    private final ConfigParameterRepositoryPort configParameterRepositoryPort;
    private final ConfigChangeLogRepositoryPort configChangeLogRepositoryPort;

    public ConfigService(ConfigParameterRepositoryPort configParameterRepositoryPort,
                         ConfigChangeLogRepositoryPort configChangeLogRepositoryPort) {
        this.configParameterRepositoryPort = configParameterRepositoryPort;
        this.configChangeLogRepositoryPort = configChangeLogRepositoryPort;
    }

    public ConfigParameter updateParameter(String key, String newValue, User changedBy) {
        ConfigParameter parameter = configParameterRepositoryPort.findByKey(key)
                .orElseThrow();
        String oldValue = parameter.getValue();
        parameter.setValue(newValue);
        ConfigParameter saved = configParameterRepositoryPort.save(parameter);

        ConfigChangeLog log = ConfigChangeLog.builder()
                .key(key)
                .oldValue(oldValue)
                .newValue(newValue)
                .changedBy(changedBy)
                .changedAt(Instant.now())
                .build();
        configChangeLogRepositoryPort.save(log);
        return saved;
    }
}
