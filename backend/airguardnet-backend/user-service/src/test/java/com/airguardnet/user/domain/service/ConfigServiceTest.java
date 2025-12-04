// Cobertura matriz Nro 41
package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.model.ConfigChangeLog;
import com.airguardnet.user.domain.model.ConfigParameter;
import com.airguardnet.user.domain.model.User;
import com.airguardnet.user.domain.repository.ConfigChangeLogRepositoryPort;
import com.airguardnet.user.domain.repository.ConfigParameterRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfigServiceTest {

    @Mock
    private ConfigParameterRepositoryPort configParameterRepositoryPort;

    @Mock
    private ConfigChangeLogRepositoryPort configChangeLogRepositoryPort;

    @InjectMocks
    private ConfigService configService;

    @Test
    // Nro 41: Crear registro de config_change_log al actualizar parámetro
    void updateParameter_createsChangeLog() {
        ConfigParameter parameter = ConfigParameter.builder()
                .key("DEVICE_OFFLINE_MINUTES")
                .value("10")
                .build();
        User admin = User.builder().id(1L).role("ADMIN").build();

        when(configParameterRepositoryPort.findByKey("DEVICE_OFFLINE_MINUTES")).thenReturn(Optional.of(parameter));
        when(configParameterRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ConfigParameter result = configService.updateParameter("DEVICE_OFFLINE_MINUTES", "15", admin);

        verify(configChangeLogRepositoryPort).save(any(ConfigChangeLog.class));
        assertEquals("15", result.getValue());
    }
}
