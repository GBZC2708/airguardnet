package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.repository.SystemLogRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SystemLogServiceTest {

    @Mock
    private SystemLogRepositoryPort systemLogRepositoryPort;

    @InjectMocks
    private SystemLogService systemLogService;

    @Test
    // Nro 43: Registrar log de sistema tipo ERROR
    void logError_savesErrorType() {
        systemLogService.logError("device-service", "Intento de lectura desde dispositivo desconocido");

        verify(systemLogRepositoryPort).save(argThat(log -> "ERROR".equals(log.getType())));
    }
}
