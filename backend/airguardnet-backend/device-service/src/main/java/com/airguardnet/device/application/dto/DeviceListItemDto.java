package com.airguardnet.device.application.dto;

import lombok.Data;

@Data
public class DeviceListItemDto {
    private Long id;
    private String name;
    private String status;
    private Double lastBatteryLevel;
}
