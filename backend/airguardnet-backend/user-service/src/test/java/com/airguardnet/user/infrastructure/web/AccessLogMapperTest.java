package com.airguardnet.user.infrastructure.web;

import com.airguardnet.user.domain.model.AccessLog;
import com.airguardnet.user.domain.model.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccessLogMapperTest {

    private final AccessLogMapper mapper = new AccessLogMapper();

    @Test
    // Nro 70: Validar mapeo de AccessLog a DTO para pantalla de logs
    void toDto_mapsEmailAndAction() {
        User user = User.builder().email("gerald.admin@airguardnet.local").build();
        AccessLog log = AccessLog.builder()
                .user(user)
                .userId(1L)
                .action("LOGIN")
                .ipAddress("127.0.0.1")
                .createdAt(Instant.now())
                .build();

        AccessLogDTO dto = mapper.toDto(log);

        assertEquals("gerald.admin@airguardnet.local", dto.getUserEmail());
        assertEquals("LOGIN", dto.getAction());
    }
}
