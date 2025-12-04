// Cobertura matriz Nro 51
package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.SensorConfig;
import com.airguardnet.device.domain.repository.SensorConfigRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SensorConfigServiceTest {

    @Mock
    private SensorConfigRepositoryPort sensorConfigRepositoryPort;

    @InjectMocks
    private SensorConfigService sensorConfigService;

    @Test
    // Nro 51: Validar que sensor_configs se guarde con unidad µg/m³
    void create_setsMicrogramUnit() {
        when(sensorConfigRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SensorConfig config = sensorConfigService.create("PM25", 35, 75, 1L);

        assertEquals("µg/m³", config.getUnit());
    }
}
