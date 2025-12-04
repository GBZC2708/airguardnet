package com.airguardnet.device.application.usecase;

import com.airguardnet.device.domain.model.SensorConfig;
import com.airguardnet.device.domain.repository.SensorConfigRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateSensorConfigUseCaseTest {

    @Mock
    private SensorConfigRepositoryPort sensorConfigRepositoryPort;

    @InjectMocks
    private UpdateSensorConfigUseCase useCase;

    @Test
    void execute_updatesValuesWhenFound() {
        SensorConfig config = new SensorConfig();
        config.setId(5L);
        config.setSensorType("PM25");
        config.setRecommendedMax(30.0);
        config.setCriticalThreshold(60.0);
        config.setUnit("old");

        when(sensorConfigRepositoryPort.findById(5L)).thenReturn(Optional.of(config));
        when(sensorConfigRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SensorConfig updated = useCase.execute(5L, 35.0, 75.0, null);

        assertEquals(35.0, updated.getRecommendedMax());
        assertEquals(75.0, updated.getCriticalThreshold());
        assertEquals("old", updated.getUnit());
    }

    @Test
    void execute_throwsWhenMissing() {
        when(sensorConfigRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(99L, 10.0, 20.0, "µg/m³"));
    }
}
