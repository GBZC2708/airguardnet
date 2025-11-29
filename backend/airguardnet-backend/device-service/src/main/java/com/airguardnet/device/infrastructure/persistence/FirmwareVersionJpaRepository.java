package com.airguardnet.device.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FirmwareVersionJpaRepository extends JpaRepository<FirmwareVersionEntity, Long> {
}
