package com.airguardnet.user.domain.model;

import lombok.Data;

@Data
public class UserSummaryReport {
    private Long userId;
    private int totalReadings;
    private double avgPm25;
}
