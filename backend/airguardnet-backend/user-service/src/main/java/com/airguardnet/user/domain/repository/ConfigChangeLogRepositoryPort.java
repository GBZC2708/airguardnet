package com.airguardnet.user.domain.repository;

import com.airguardnet.user.domain.model.ConfigChangeLog;

public interface ConfigChangeLogRepositoryPort {
    ConfigChangeLog save(ConfigChangeLog log);
}
