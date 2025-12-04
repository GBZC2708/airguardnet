package com.airguardnet.device.application.mapper;

import com.airguardnet.device.application.dto.ReadingDto;
import com.airguardnet.device.domain.model.Reading;

public class ReadingMapper {

    public ReadingDto toDto(Reading entity) {
        ReadingDto dto = new ReadingDto();
        dto.setId(entity.getId());
        dto.setDeviceId(entity.getDeviceId());
        dto.setPm25(entity.getPm25());
        dto.setRiskIndex(entity.getRiskIndex());
        return dto;
    }
}
