// Cobertura matriz Nro 47
package com.airguardnet.device.application.mapper;

import com.airguardnet.device.application.dto.ReadingDto;
import com.airguardnet.device.domain.model.Reading;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadingMapperTest {

    private final ReadingMapper readingMapper = new ReadingMapper();

    @Test
    // Nro 47: Construir DTO de lectura desde entidad
    void toDto_mapsBasicFields() {
        Reading reading = Reading.builder()
                .id(1L)
                .deviceId(10L)
                .pm25(48.2)
                .riskIndex(60)
                .build();

        ReadingDto dto = readingMapper.toDto(reading);

        assertEquals(reading.getId(), dto.getId());
        assertEquals(reading.getDeviceId(), dto.getDeviceId());
        assertEquals(reading.getPm25(), dto.getPm25());
        assertEquals(reading.getRiskIndex(), dto.getRiskIndex());
    }
}
