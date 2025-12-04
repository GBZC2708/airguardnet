package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.Device;
import com.airguardnet.device.domain.model.SensorNote;
import com.airguardnet.device.domain.repository.SensorNoteRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SensorNoteServiceTest {

    @Mock
    private SensorNoteRepositoryPort sensorNoteRepositoryPort;

    @InjectMocks
    private SensorNoteService sensorNoteService;

    @Test
    // Nro 52: Crear nota de sensor asociada a dispositivo
    void createNote_storesAssociation() {
        Device device = Device.builder().id(1L).deviceUid("AG-ESP32-0001").build();
        when(sensorNoteRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SensorNote note = sensorNoteService.createNote(device, "PM25", "sensor cambiado", 2L);

        assertEquals("sensor cambiado", note.getNoteText());
    }
}
