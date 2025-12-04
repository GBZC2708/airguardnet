package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.Device;
import com.airguardnet.device.domain.model.SensorNote;
import com.airguardnet.device.domain.repository.SensorNoteRepositoryPort;

import java.time.Instant;

public class SensorNoteService {

    private final SensorNoteRepositoryPort sensorNoteRepositoryPort;

    public SensorNoteService(SensorNoteRepositoryPort sensorNoteRepositoryPort) {
        this.sensorNoteRepositoryPort = sensorNoteRepositoryPort;
    }

    public SensorNote createNote(Device device, String type, String text, Object user) {
        SensorNote note = SensorNote.builder()
                .device(device)
                .sensorType(type)
                .noteText(text)
                .createdById(user instanceof Number number ? number.longValue() : null)
                .createdAt(Instant.now())
                .build();
        return sensorNoteRepositoryPort.save(note);
    }
}
