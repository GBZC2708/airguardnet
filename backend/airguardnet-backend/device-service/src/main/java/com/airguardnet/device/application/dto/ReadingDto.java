package com.airguardnet.device.application.dto;

import lombok.Data;

@Data
public class ReadingDto {
    private Long id;
    private Long deviceId;
    private Double pm25;
    private Integer riskIndex;
}
