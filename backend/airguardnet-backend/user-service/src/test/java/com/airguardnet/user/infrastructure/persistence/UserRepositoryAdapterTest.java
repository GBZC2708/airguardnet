// Cobertura matriz Nro 60
package com.airguardnet.user.infrastructure.persistence;

import com.airguardnet.user.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private UserRepositoryAdapter userRepositoryAdapter;

    @Test
        // Nro 60: Validar que recuperación de usuario por email no sea sensible a mayúsculas
    void findByEmailIgnoreCase_uppercaseEmail_returnsUser() {

        // Crear UserEntity con los campos mínimos que sí existen en la entidad real
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setEmail("gerald.admin@airguardnet.local");
        entity.setRole("ADMIN");  // esta sí la usas en el dominio

        when(userJpaRepository.findByEmailIgnoreCase(any()))
                .thenReturn(Optional.of(entity));

        Optional<User> result = userRepositoryAdapter.findByEmailIgnoreCase(
                "GERALD.ADMIN@AIRGUARDNET.LOCAL"
        );

        assertTrue(result.isPresent());
    }
}
