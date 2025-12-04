// Cobertura matriz Nro 66–67
package com.airguardnet.device.domain.service;

import com.airguardnet.device.domain.model.FirmwareVersion;
import com.airguardnet.device.domain.repository.FirmwareVersionRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FirmwareServiceTest {

    @Mock
    private FirmwareVersionRepositoryPort firmwareVersionRepositoryPort;

    @InjectMocks
    private FirmwareService firmwareService;

    @Test
    // Nro 66: Validar que creación de firmware version marque recommended por defecto
    void createFirmware_recommendedTrue() {
        when(firmwareVersionRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        FirmwareVersion version = firmwareService.create("1.2.0", "Nueva versión", true);

        assertTrue(version.isRecommended());
    }

    @Test
    // Nro 67: Editar descripción de firmware existente
    void updateDescription_changesText() {
        FirmwareVersion version = FirmwareVersion.builder().versionCode("1.0.0").description("Old").build();
        when(firmwareVersionRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        FirmwareVersion updated = firmwareService.updateDescription(version, "Mejora batería");

        assertEquals("Mejora batería", updated.getDescription());
    }
}
