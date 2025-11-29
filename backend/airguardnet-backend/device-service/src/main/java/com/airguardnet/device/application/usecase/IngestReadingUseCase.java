package com.airguardnet.device.application.usecase;

import com.airguardnet.common.exception.NotFoundException;
import com.airguardnet.common.exception.ValidationException;
import com.airguardnet.device.domain.model.Alert;
import com.airguardnet.device.domain.model.Device;
import com.airguardnet.device.domain.model.Reading;
import com.airguardnet.device.domain.model.SensorConfig;
import com.airguardnet.device.domain.repository.AlertRepositoryPort;
import com.airguardnet.device.domain.repository.DeviceRepositoryPort;
import com.airguardnet.device.domain.repository.ReadingRepositoryPort;
import com.airguardnet.device.domain.repository.SensorConfigRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IngestReadingUseCase {
    private static final Logger log = LoggerFactory.getLogger(IngestReadingUseCase.class);

    private final DeviceRepositoryPort deviceRepositoryPort;
    private final SensorConfigRepositoryPort sensorConfigRepositoryPort;
    private final ReadingRepositoryPort readingRepositoryPort;
    private final AlertRepositoryPort alertRepositoryPort;

    public IngestReadingResult ingest(IngestReadingCommand command) {
        if (command.getDeviceUid() == null || command.getDeviceUid().isBlank()) {
            throw new ValidationException("device_uid is required");
        }
        Device device = deviceRepositoryPort.findByDeviceUid(command.getDeviceUid())
                .orElseThrow(() -> new NotFoundException("Device not found"));

        Double pm1 = normalizePm(command.getPm1());
        Double pm25 = normalizePm(command.getPm25());
        Double pm10 = normalizePm(command.getPm10());
        Double battery = normalizeBattery(command.getBatteryLevel());

        int riskIndex = calculateRiskIndex(pm25);
        int airQualityPercent = Math.max(0, Math.min(100, 100 - riskIndex));

        Reading reading = Reading.builder()
                .deviceId(device.getId())
                .recordedAt(Instant.now())
                .pm1(pm1)
                .pm25(pm25)
                .pm10(pm10)
                .batteryLevel(battery)
                .riskIndex(riskIndex)
                .airQualityPercent(airQualityPercent)
                .build();
        reading = readingRepositoryPort.save(reading);

        String newStatus = deriveStatus(riskIndex);
        Device updated = Device.builder()
                .id(device.getId())
                .deviceUid(device.getDeviceUid())
                .name(device.getName())
                .status(newStatus)
                .assignedUserId(device.getAssignedUserId())
                .lastCommunicationAt(Instant.now())
                .lastBatteryLevel(battery != null ? battery : device.getLastBatteryLevel())
                .currentFirmwareId(device.getCurrentFirmwareId())
                .createdAt(device.getCreatedAt())
                .updatedAt(Instant.now())
                .build();
        deviceRepositoryPort.save(updated);

        Alert createdAlert = maybeCreateAlert(pm25, device.getId(), reading.getId());
        return new IngestReadingResult(reading.getId(), device.getId(), riskIndex, airQualityPercent, newStatus, createdAlert);
    }

    private Double normalizePm(Double value) {
        if (value == null) {
            return null;
        }
        if (value < 0) {
            return 0.0;
        }
        if (value > 2000) {
            log.warn("PM value {} exceeded max, clamping to 2000", value);
            return 2000.0;
        }
        return value;
    }

    private Double normalizeBattery(Double value) {
        if (value == null) {
            return null;
        }
        if (value < 0) return 0.0;
        if (value > 100) return 100.0;
        return value;
    }

    private int calculateRiskIndex(Double pm25) {
        if (pm25 == null) {
            return 0;
        }
        Optional<SensorConfig> configOpt = sensorConfigRepositoryPort.findBySensorType("PM25");
        if (configOpt.isPresent()) {
            SensorConfig config = configOpt.get();
            double recommended = config.getRecommendedMax();
            double critical = config.getCriticalThreshold();
            if (pm25 <= recommended) {
                return (int) Math.round((pm25 / recommended) * 25);
            } else if (pm25 < critical) {
                double ratio = (pm25 - recommended) / (critical - recommended);
                return 26 + (int) Math.round(ratio * (70 - 26));
            } else {
                double ratio = Math.min((pm25 - critical) / critical, 1.0);
                return Math.min(100, 71 + (int) Math.round(ratio * (100 - 71)));
            }
        }
        if (pm25 <= 35) {
            return (int) Math.round((pm25 / 35.0) * 25);
        } else if (pm25 <= 75) {
            double ratio = (pm25 - 35) / 40.0;
            return 26 + (int) Math.round(ratio * (70 - 26));
        } else if (pm25 <= 150) {
            double ratio = (pm25 - 75) / 75.0;
            return 71 + (int) Math.round(ratio * (90 - 71));
        } else {
            double ratio = Math.min((pm25 - 150) / 50.0, 1.0);
            return 91 + (int) Math.round(ratio * (100 - 91));
        }
    }

    private String deriveStatus(int riskIndex) {
        if (riskIndex <= 25) {
            return "ACTIVE";
        } else if (riskIndex <= 70) {
            return "WARNING";
        }
        return "CRITICAL";
    }

    private Alert maybeCreateAlert(Double pm25, Long deviceId, Long readingId) {
        if (pm25 == null) {
            return null;
        }
        Optional<SensorConfig> configOpt = sensorConfigRepositoryPort.findBySensorType("PM25");
        double recommended = 35;
        double critical = 75;
        if (configOpt.isPresent()) {
            recommended = configOpt.get().getRecommendedMax();
            critical = configOpt.get().getCriticalThreshold();
        }
        if (pm25 > critical) {
            Alert alert = Alert.builder()
                    .deviceId(deviceId)
                    .readingId(readingId)
                    .severity("CRITICAL")
                    .status("PENDING")
                    .message("PM2.5 en " + pm25 + " µg/m³ supera umbral " + critical)
                    .createdAt(Instant.now())
                    .build();
            return alertRepositoryPort.save(alert);
        }
        if (pm25 > recommended) {
            Alert alert = Alert.builder()
                    .deviceId(deviceId)
                    .readingId(readingId)
                    .severity("HIGH")
                    .status("PENDING")
                    .message("PM2.5 en " + pm25 + " µg/m³ supera umbral " + recommended)
                    .createdAt(Instant.now())
                    .build();
            return alertRepositoryPort.save(alert);
        }
        return null;
    }
}
