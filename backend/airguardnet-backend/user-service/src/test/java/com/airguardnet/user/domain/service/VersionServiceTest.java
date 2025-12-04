// Cobertura matriz Nro 44 y 62
package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.model.VersionHistory;
import com.airguardnet.user.domain.repository.VersionHistoryRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VersionServiceTest {

    @Mock
    private VersionHistoryRepositoryPort versionHistoryRepositoryPort;

    @InjectMocks
    private VersionService versionService;

    @Test
    // Nro 44: Registrar nueva versión en version_history
    void registerVersion_savesVersion() {
        when(versionHistoryRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        VersionHistory history = versionService.registerVersion("v1.2.0", "Nueva versión de pruebas");

        assertEquals("v1.2.0", history.getVersionNumber());
    }

    @Test
    // Nro 62: Validar que el número de versión mostrado provenga del último registro
    void getLatest_returnsLatestVersion() {
        VersionHistory v2 = VersionHistory.builder().versionNumber("v1.1.0").build();
        when(versionHistoryRepositoryPort.findTopByOrderByReleasedAtDesc()).thenReturn(Optional.of(v2));

        VersionHistory latest = versionService.getLatest();

        assertEquals("v1.1.0", latest.getVersionNumber());
    }
}
