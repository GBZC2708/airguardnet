package com.airguardnet.user.infrastructure.persistence;

import com.airguardnet.user.domain.model.ConfigParameter;
import com.airguardnet.user.domain.repository.ConfigParameterRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConfigParameterRepositoryAdapter implements ConfigParameterRepositoryPort {
    private final ConfigParameterJpaRepository configParameterJpaRepository;

    @Override
    public List<ConfigParameter> findAll() {
        return configParameterJpaRepository.findAll()
                .stream()
                .map(ConfigParameterEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ConfigParameter> findByKey(String key) {
        return configParameterJpaRepository.findByKey(key).map(ConfigParameterEntity::toDomain);
    }

    @Override
    public ConfigParameter save(ConfigParameter parameter) {
        ConfigParameterEntity entity = new ConfigParameterEntity();
        entity.setId(parameter.getId());
        entity.setKey(parameter.getKey());
        entity.setValue(parameter.getValue());
        entity.setCreatedAt(parameter.getCreatedAt());
        entity.setUpdatedAt(parameter.getUpdatedAt());
        return configParameterJpaRepository.save(entity).toDomain();
    }
}
