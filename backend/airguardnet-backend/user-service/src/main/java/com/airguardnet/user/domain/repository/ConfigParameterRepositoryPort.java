package com.airguardnet.user.domain.repository;

import com.airguardnet.user.domain.model.ConfigParameter;

import java.util.Optional;

public interface ConfigParameterRepositoryPort {
    Optional<ConfigParameter> findByKey(String key);

    ConfigParameter save(ConfigParameter parameter);
}
