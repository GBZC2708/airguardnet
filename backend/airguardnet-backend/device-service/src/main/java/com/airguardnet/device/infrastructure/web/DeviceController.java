package com.airguardnet.device.infrastructure.web;

import com.airguardnet.common.web.ApiResponse;
import com.airguardnet.device.application.usecase.GetDeviceUseCase;
import com.airguardnet.device.application.usecase.ListDeviceAlertsUseCase;
import com.airguardnet.device.application.usecase.ListDeviceReadingsUseCase;
import com.airguardnet.device.application.usecase.ListDevicesUseCase;
import com.airguardnet.device.domain.model.Alert;
import com.airguardnet.device.domain.model.Device;
import com.airguardnet.device.domain.model.Reading;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final ListDevicesUseCase listDevicesUseCase;
    private final GetDeviceUseCase getDeviceUseCase;
    private final ListDeviceReadingsUseCase listDeviceReadingsUseCase;
    private final ListDeviceAlertsUseCase listDeviceAlertsUseCase;

    public DeviceController(ListDevicesUseCase listDevicesUseCase,
                            GetDeviceUseCase getDeviceUseCase,
                            ListDeviceReadingsUseCase listDeviceReadingsUseCase,
                            ListDeviceAlertsUseCase listDeviceAlertsUseCase) {
        this.listDevicesUseCase = listDevicesUseCase;
        this.getDeviceUseCase = getDeviceUseCase;
        this.listDeviceReadingsUseCase = listDeviceReadingsUseCase;
        this.listDeviceAlertsUseCase = listDeviceAlertsUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Device>>> listDevices() {
        return ResponseEntity.ok(ApiResponse.success(listDevicesUseCase.execute()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Device>> getDevice(@PathVariable Long id) {
        return getDeviceUseCase.execute(id)
                .map(device -> ResponseEntity.ok(ApiResponse.success(device)))
                .orElseGet(() -> ResponseEntity.status(404).body(ApiResponse.error("Device not found")));
    }

    @GetMapping("/{id}/readings")
    public ResponseEntity<ApiResponse<List<Reading>>> getDeviceReadings(@PathVariable Long id,
                                                                        @RequestParam(value = "limit", required = false) Integer limit) {
        List<Reading> readings = listDeviceReadingsUseCase.execute(id, limit);
        return ResponseEntity.ok(ApiResponse.success(readings));
    }

    @GetMapping("/{id}/alerts")
    public ResponseEntity<ApiResponse<List<Alert>>> getDeviceAlerts(@PathVariable Long id,
                                                                    @RequestParam(value = "limit", required = false) Integer limit) {
        List<Alert> alerts = listDeviceAlertsUseCase.execute(id);
        if (limit != null && limit > 0 && alerts.size() > limit) {
            alerts = alerts.subList(0, limit);
        }
        return ResponseEntity.ok(ApiResponse.success(alerts));
    }
}
