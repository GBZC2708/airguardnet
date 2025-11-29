package com.airguardnet.user.domain.repository;

import com.airguardnet.user.domain.model.AccessLog;

public interface AccessLogRepositoryPort {
    AccessLog save(AccessLog log);
}
