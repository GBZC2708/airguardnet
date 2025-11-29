package com.airguardnet.user.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigParameterJpaRepository extends JpaRepository<ConfigParameterEntity, Long> {
    Optional<ConfigParameterEntity> findByKey(String key);
}
