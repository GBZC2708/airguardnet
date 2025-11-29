package com.airguardnet.device.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentJpaRepository extends JpaRepository<IncidentEntity, Long> {
}
