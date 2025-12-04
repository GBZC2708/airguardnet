// Cobertura matriz Nro 16–20
package com.airguardnet.device.application.usecase;

import com.airguardnet.device.domain.model.Alert;
import com.airguardnet.device.domain.model.Device;
import com.airguardnet.device.domain.model.Reading;
import com.airguardnet.device.domain.repository.AlertRepositoryPort;
import com.airguardnet.device.domain.repository.DeviceRepositoryPort;
import com.airguardnet.device.domain.repository.ReadingRepositoryPort;
import com.airguardnet.device.domain.repository.SensorConfigRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IngestReadingNormalizationTest {

    @Mock
    private DeviceRepositoryPort deviceRepositoryPort;
    @Mock
    private SensorConfigRepositoryPort sensorConfigRepositoryPort;
    @Mock
    private ReadingRepositoryPort readingRepositoryPort;
    @Mock
    private AlertRepositoryPort alertRepositoryPort;

    private IngestReadingUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new IngestReadingUseCase(deviceRepositoryPort, sensorConfigRepositoryPort, readingRepositoryPort, alertRepositoryPort);

        Device device = Device.builder()
                .id(1L)
                .deviceUid("AG-1")
                .name("Device")
                .status("ACTIVE")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        when(deviceRepositoryPort.findByDeviceUid("AG-1")).thenReturn(Optional.of(device));
        when(deviceRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        when(sensorConfigRepositoryPort.findBySensorType("PM25")).thenReturn(Optional.empty());
        when(alertRepositoryPort.save(any(Alert.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(readingRepositoryPort.save(any(Reading.class))).thenAnswer(invocation -> {
            Reading reading = invocation.getArgument(0);
            return Reading.builder()
                    .id(10L)
                    .deviceId(reading.getDeviceId())
                    .recordedAt(reading.getRecordedAt())
                    .pm1(reading.getPm1())
                    .pm25(reading.getPm25())
                    .pm10(reading.getPm10())
                    .batteryLevel(reading.getBatteryLevel())
                    .riskIndex(reading.getRiskIndex())
                    .airQualityPercent(reading.getAirQualityPercent())
                    .build();
        });
    }

    private IngestReadingCommand baseCommand() {
        IngestReadingCommand command = new IngestReadingCommand();
        command.setDeviceUid("AG-1");
        command.setPm1(10.0);
        command.setPm10(12.0);
        command.setTimestamp(Instant.now().toString());
        return command;
    }

    // Nro 16: Normalizar valor negativo de PM2.5 a 0
    @Test
    void normalizePm_valorNegativo_aCero() {
        IngestReadingCommand command = baseCommand();
        command.setPm25(-5.0);

        useCase.ingest(command);

        ArgumentCaptor<Reading> captor = ArgumentCaptor.forClass(Reading.class);
        verify(readingRepositoryPort).save(captor.capture());
        assertEquals(0.0, captor.getValue().getPm25());
    }

    // Nro 17: Normalizar valor extremadamente alto de PM2.5 a 2000
    @Test
    void normalizePm_valorAlto_aMaximo() {
        IngestReadingCommand command = baseCommand();
        command.setPm25(3000.0);

        useCase.ingest(command);

        ArgumentCaptor<Reading> captor = ArgumentCaptor.forClass(Reading.class);
        verify(readingRepositoryPort).save(captor.capture());
        assertEquals(2000.0, captor.getValue().getPm25());
    }

    // Nro 18: Normalizar valor de batería >100 a 100
    @Test
    void normalizeBattery_mayorQueCien_aCien() {
        IngestReadingCommand command = baseCommand();
        command.setPm25(35.0);
        command.setBatteryLevel(150.0);

        useCase.ingest(command);

        ArgumentCaptor<Reading> captor = ArgumentCaptor.forClass(Reading.class);
        verify(readingRepositoryPort).save(captor.capture());
        assertEquals(100.0, captor.getValue().getBatteryLevel());
    }

    // Nro 19: Normalizar batería negativa a 0
    @Test
    void normalizeBattery_negativo_aCero() {
        IngestReadingCommand command = baseCommand();
        command.setPm25(35.0);
        command.setBatteryLevel(-10.0);

        useCase.ingest(command);

        ArgumentCaptor<Reading> captor = ArgumentCaptor.forClass(Reading.class);
        verify(readingRepositoryPort).save(captor.capture());
        assertEquals(0.0, captor.getValue().getBatteryLevel());
    }

    // Nro 20: Calcular risk_index en tramo seguro (<=recommended_max)
    @Test
    void calculate_tramoSeguro_devuelveEntre0y25() {
        IngestReadingCommand command = baseCommand();
        command.setPm25(17.5);
        command.setBatteryLevel(80.0);

        IngestReadingResult result = useCase.ingest(command);

        assertTrue(result.getRiskIndex() >= 0 && result.getRiskIndex() <= 25);
    }
}
