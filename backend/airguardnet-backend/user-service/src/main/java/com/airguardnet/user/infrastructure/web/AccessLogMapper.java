package com.airguardnet.user.infrastructure.web;

import com.airguardnet.user.domain.model.AccessLog;

public class AccessLogMapper {

    public AccessLogDTO toDto(AccessLog log) {
        return AccessLogDTO.fromDomain(log);
    }
}
