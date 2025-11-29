package com.airguardnet.user.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class VersionHistory {
    private Long id;
    private String versionNumber;
    private String description;
    private Instant releasedAt;
}
