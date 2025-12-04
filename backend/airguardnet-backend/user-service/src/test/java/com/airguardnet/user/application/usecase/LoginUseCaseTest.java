// Cobertura matriz Nro 9–13
package com.airguardnet.user.application.usecase;

import com.airguardnet.common.exception.UnauthorizedException;
import com.airguardnet.user.domain.model.AccessLog;
import com.airguardnet.user.domain.model.SystemLog;
import com.airguardnet.user.domain.model.User;
import com.airguardnet.user.domain.repository.AccessLogRepositoryPort;
import com.airguardnet.user.domain.repository.SystemLogRepositoryPort;
import com.airguardnet.user.domain.repository.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // <<< CLAVE: evitar UnnecessaryStubbingException
class LoginUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AccessLogRepositoryPort accessLogRepositoryPort;
    @Mock
    private SystemLogRepositoryPort systemLogRepositoryPort;

    private LoginUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new LoginUseCase(userRepositoryPort, passwordEncoder, accessLogRepositoryPort, systemLogRepositoryPort);
        when(userRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(accessLogRepositoryPort.save(any(AccessLog.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(systemLogRepositoryPort.save(any(SystemLog.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    // Nro 9: Incrementar contador de intentos fallidos
    @Test
    void increaseFailedAttempts_incrementaEnUno() {
        User user = baseUser().failedLoginCount(0).build();
        when(userRepositoryPort.findByEmail("admin@airguardnet.local")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> useCase.login("admin@airguardnet.local", "wrong", "127.0.0.1"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryPort).save(captor.capture());
        assertEquals(1, captor.getValue().getFailedLoginCount());
    }

    // Nro 10: Bloquear cuenta al superar MAX_FAILED_LOGINS
    @Test
    void lockIfNecessary_superaUmbral_bloquea() {
        User user = baseUser().failedLoginCount(4).build();
        when(userRepositoryPort.findByEmail("admin@airguardnet.local")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> useCase.login("admin@airguardnet.local", "wrong", "127.0.0.1"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryPort).save(captor.capture());
        assertNotNull(captor.getValue().getLockedUntil());
    }

    // Nro 11: No bloquear si aún no se supera umbral
    @Test
    void lockIfNecessary_menorUmbral_noBloquea() {
        User user = baseUser().failedLoginCount(2).build();
        when(userRepositoryPort.findByEmail("admin@airguardnet.local")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> useCase.login("admin@airguardnet.local", "wrong", "127.0.0.1"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryPort).save(captor.capture());
        assertNull(captor.getValue().getLockedUntil());
    }

    // Nro 12: Verificar que usuario activo puede autenticar (adaptado a login no bloqueado)
    @Test
    void canLogin_activoYNoBloqueado_true() {
        User user = baseUser()
                .status("ACTIVE")
                .lockedUntil(Instant.now().minusSeconds(60))
                .passwordHash("hash")
                .build();
        when(userRepositoryPort.findByEmail("admin@airguardnet.local")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(eq("correct"), any())).thenReturn(true);

        LoginResult result = useCase.login("admin@airguardnet.local", "correct", "127.0.0.1");

        assertNotNull(result);
    }

    // Nro 13: Usuario bloqueado no puede autenticar (adaptación de DISABLED)
    @Test
    void canLogin_usuarioBloqueado_false() {
        User user = baseUser()
                .status("DISABLED")
                .lockedUntil(Instant.now().plusSeconds(120))
                .build();
        when(userRepositoryPort.findByEmail("admin@airguardnet.local")).thenReturn(Optional.of(user));

        assertThrows(UnauthorizedException.class, () -> useCase.login("admin@airguardnet.local", "any", "127.0.0.1"));
        verify(userRepositoryPort, never()).save(any(User.class));
    }

    private User.UserBuilder baseUser() {
        return User.builder()
                .id(1L)
                .name("Admin")
                .lastName("User")
                .email("admin@airguardnet.local")
                .role("ADMIN")
                .status("ACTIVE")
                .planId(1L)
                .createdAt(Instant.now());
    }
}
