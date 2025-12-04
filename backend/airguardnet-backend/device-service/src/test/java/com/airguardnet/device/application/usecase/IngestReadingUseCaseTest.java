// Cobertura matriz Nro 48–50
package com.airguardnet.device.application.usecase;

import com.airguardnet.common.exception.NotFoundException;
import com.airguardnet.device.domain.model.Alert;
import com.airguardnet.device.domain.model.Device;
import com.airguardnet.device.domain.model.Reading;
import com.airguardnet.device.domain.model.SensorConfig;
import com.airguardnet.device.domain.repository.AlertRepositoryPort;
import com.airguardnet.device.domain.repository.DeviceRepositoryPort;
import com.airguardnet.device.domain.repository.ReadingRepositoryPort;
import com.airguardnet.device.domain.repository.SensorConfigRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngestReadingUseCaseTest {

    @Mock
    private DeviceRepositoryPort deviceRepositoryPort;

    @Mock
    private SensorConfigRepositoryPort sensorConfigRepositoryPort;

    @Mock
    private ReadingRepositoryPort readingRepositoryPort;

    @Mock
    private AlertRepositoryPort alertRepositoryPort;

    @InjectMocks
    private IngestReadingUseCase useCase;

    private Device device;

    @BeforeEach
    void setUp() {
        device = Device.builder()
                .id(1L)
                .deviceUid("AG-DEVICE-01")
                .name("Device 01")
                .status("ACTIVE")
                .createdAt(Instant.now())
                .build();
    }

    @Test
    // Nro 49: Validar que IngestReadingUseCase lance error si device no existe
    void ingest_unknownDevice_throwsNotFound() {
        IngestReadingCommand command = new IngestReadingCommand();
        command.setDeviceUid("MISSING");

        when(deviceRepositoryPort.findByDeviceUid("MISSING")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.ingest(command));
    }

    @Test
    void ingest_highPmCreatesCriticalAlert() {
        IngestReadingCommand command = new IngestReadingCommand();
        command.setDeviceUid(device.getDeviceUid());
        command.setPm25(120.0);
        command.setBatteryLevel(90.0);

        SensorConfig config = new SensorConfig();
        config.setSensorType("PM25");
        config.setRecommendedMax(35.0);
        config.setCriticalThreshold(75.0);

        when(deviceRepositoryPort.findByDeviceUid(device.getDeviceUid())).thenReturn(Optional.of(device));
        when(sensorConfigRepositoryPort.findBySensorType("PM25")).thenReturn(Optional.of(config));
        when(readingRepositoryPort.save(any())).thenAnswer(inv -> {
            Reading r = inv.getArgument(0);
            r.setId(99L);
            return r;
        });
        when(alertRepositoryPort.save(any())).thenAnswer(inv -> {
            Alert alert = inv.getArgument(0);
            alert.setId(50L);
            return alert;
        });

        IngestReadingResult result = useCase.ingest(command);

        assertEquals("CRITICAL", result.getDeviceStatus());
        assertNotNull(result.getCreatedAlert());
        assertEquals("CRITICAL", result.getCreatedAlert().getSeverity());
        verify(alertRepositoryPort).save(any(Alert.class));
    }

    @Test
    // Nro 50: Validar que lectura con pm25 null no genera alerta
    void ingest_nullPm25ProducesNoAlertAndZeroRisk() {
        IngestReadingCommand command = new IngestReadingCommand();
        command.setDeviceUid(device.getDeviceUid());
        command.setPm25(null);
        command.setBatteryLevel(50.0);

        when(deviceRepositoryPort.findByDeviceUid(device.getDeviceUid())).thenReturn(Optional.of(device));
        when(sensorConfigRepositoryPort.findBySensorType("PM25")).thenReturn(Optional.empty());
        when(readingRepositoryPort.save(any())).thenAnswer(inv -> {
            Reading r = inv.getArgument(0);
            r.setId(10L);
            return r;
        });

        IngestReadingResult result = useCase.ingest(command);

        assertEquals(0, result.getRiskIndex());
        assertEquals(100, result.getAirQualityPercent());
        assertNull(result.getCreatedAlert());
        verify(alertRepositoryPort, never()).save(any());
    }

    @Test
    void ingest_lowPmKeepsDeviceActive() {
        IngestReadingCommand command = new IngestReadingCommand();
        command.setDeviceUid(device.getDeviceUid());
        command.setPm25(10.0);
        command.setBatteryLevel(40.0);

        when(deviceRepositoryPort.findByDeviceUid(device.getDeviceUid())).thenReturn(Optional.of(device));
        when(sensorConfigRepositoryPort.findBySensorType("PM25")).thenReturn(Optional.empty());
        when(readingRepositoryPort.save(any())).thenAnswer(inv -> {
            Reading r = inv.getArgument(0);
            r.setId(11L);
            return r;
        });

        IngestReadingResult result = useCase.ingest(command);

        assertEquals("ACTIVE", result.getDeviceStatus());
        assertTrue(result.getRiskIndex() <= 25);
        verify(deviceRepositoryPort).save(any(Device.class));
    }

    @Test
    // Nro 48: Validar que lectura creada tenga recorded_at no nulo
    void ingest_setsRecordedAt() {
        IngestReadingCommand command = new IngestReadingCommand();
        command.setDeviceUid(device.getDeviceUid());
        command.setPm25(25.0);

        when(deviceRepositoryPort.findByDeviceUid(device.getDeviceUid())).thenReturn(Optional.of(device));
        when(sensorConfigRepositoryPort.findBySensorType("PM25")).thenReturn(Optional.empty());
        when(readingRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        useCase.ingest(command);

        verify(readingRepositoryPort).save(argThat(reading -> reading.getRecordedAt() != null));
    }
}
