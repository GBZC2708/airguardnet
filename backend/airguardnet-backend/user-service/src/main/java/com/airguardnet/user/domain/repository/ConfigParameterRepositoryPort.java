package com.airguardnet.user.domain.repository;

import com.airguardnet.user.domain.model.ConfigParameter;

import java.util.Optional;
import java.util.List;

public interface ConfigParameterRepositoryPort {
    List<ConfigParameter> findAll();

    Optional<ConfigParameter> findByKey(String key);

    ConfigParameter save(ConfigParameter parameter);
}
