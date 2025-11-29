package com.airguardnet.user.domain.repository;

import com.airguardnet.user.domain.model.SystemLog;

public interface SystemLogRepositoryPort {
    SystemLog save(SystemLog log);
}
