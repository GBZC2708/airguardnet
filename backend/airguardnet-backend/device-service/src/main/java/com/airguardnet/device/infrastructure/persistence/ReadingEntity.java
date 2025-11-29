package com.airguardnet.device.infrastructure.persistence;

import com.airguardnet.device.domain.model.Reading;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "readings")
@Data
public class ReadingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long deviceId;
    private Instant recordedAt;
    private Double pm1;
    private Double pm25;
    private Double pm10;
    private Double batteryLevel;
    private Integer riskIndex;
    private Integer airQualityPercent;

    public static ReadingEntity fromDomain(Reading reading) {
        ReadingEntity entity = new ReadingEntity();
        entity.setId(reading.getId());
        entity.setDeviceId(reading.getDeviceId());
        entity.setRecordedAt(reading.getRecordedAt());
        entity.setPm1(reading.getPm1());
        entity.setPm25(reading.getPm25());
        entity.setPm10(reading.getPm10());
        entity.setBatteryLevel(reading.getBatteryLevel());
        entity.setRiskIndex(reading.getRiskIndex());
        entity.setAirQualityPercent(reading.getAirQualityPercent());
        return entity;
    }

    public Reading toDomain() {
        return Reading.builder()
                .id(id)
                .deviceId(deviceId)
                .recordedAt(recordedAt)
                .pm1(pm1)
                .pm25(pm25)
                .pm10(pm10)
                .batteryLevel(batteryLevel)
                .riskIndex(riskIndex)
                .airQualityPercent(airQualityPercent)
                .build();
    }
}
