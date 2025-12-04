package com.airguardnet.device.application.mapper;

import com.airguardnet.device.application.dto.DeviceListItemDto;
import com.airguardnet.device.domain.model.Device;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeviceMapperTest {

    private final DeviceMapper deviceMapper = new DeviceMapper();

    @Test
    // Nro 57: Construir DTO de dispositivo para lista
    void toListItem_mapsStatusAndName() {
        Device device = Device.builder()
                .id(5L)
                .name("Sensor Demo")
                .status("CRITICAL")
                .lastBatteryLevel(20.0)
                .build();

        DeviceListItemDto dto = deviceMapper.toListItem(device);

        assertEquals(device.getStatus(), dto.getStatus());
        assertEquals(device.getName(), dto.getName());
    }
}
