package com.airguardnet.user.domain.repository;

import com.airguardnet.user.domain.model.SystemLog;

import java.util.List;

public interface SystemLogRepositoryPort {
    SystemLog save(SystemLog log);

    List<SystemLog> findAllOrderedByCreatedAtDesc();
}
