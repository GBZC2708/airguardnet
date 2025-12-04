// Cobertura matriz Nro 42
package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.model.User;
import com.airguardnet.user.domain.repository.AccessLogRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccessLogServiceTest {

    @Mock
    private AccessLogRepositoryPort accessLogRepositoryPort;

    @InjectMocks
    private AccessLogService accessLogService;

    @Test
    // Nro 42: Registrar acceso de usuario
    void logAccess_savesEntry() {
        User admin = User.builder().id(1L).email("admin@test").role("ADMIN").build();

        accessLogService.logAccess(admin, "LOGIN", "127.0.0.1");

        verify(accessLogRepositoryPort).save(argThat(log ->
                log.getUser().equals(admin)
                        && "LOGIN".equals(log.getAction())
                        && "127.0.0.1".equals(log.getIpAddress())));
    }
}
