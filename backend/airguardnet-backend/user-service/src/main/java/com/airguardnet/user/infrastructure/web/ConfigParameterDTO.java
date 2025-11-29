package com.airguardnet.user.infrastructure.web;

import com.airguardnet.user.domain.model.ConfigParameter;
import com.airguardnet.user.infrastructure.persistence.ConfigParameterEntity;

public class ConfigParameterDTO {
    private Long id;
    private String key;
    private String value;

    public static ConfigParameterDTO fromEntity(ConfigParameterEntity entity) {
        ConfigParameterDTO dto = new ConfigParameterDTO();
        dto.id = entity.getId();
        dto.key = entity.getKey();
        dto.value = entity.getValue();
        return dto;
    }

    public static ConfigParameterDTO fromDomain(ConfigParameter parameter) {
        ConfigParameterDTO dto = new ConfigParameterDTO();
        dto.id = parameter.getId();
        dto.key = parameter.getKey();
        dto.value = parameter.getValue();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
