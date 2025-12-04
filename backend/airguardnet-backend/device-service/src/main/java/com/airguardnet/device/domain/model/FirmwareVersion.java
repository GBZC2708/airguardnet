package com.airguardnet.device.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FirmwareVersion {
    private Long id;
    private String versionCode;
    private String description;
    private boolean recommended;
}
