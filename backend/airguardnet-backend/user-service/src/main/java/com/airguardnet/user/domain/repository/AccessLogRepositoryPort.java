package com.airguardnet.user.domain.repository;

import com.airguardnet.user.domain.model.AccessLog;

import java.util.List;

public interface AccessLogRepositoryPort {
    AccessLog save(AccessLog log);

    List<AccessLog> findAllOrderedByCreatedAtDesc();
}
