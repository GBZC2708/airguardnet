package com.airguardnet.device.application.mapper;

import com.airguardnet.device.application.dto.DeviceListItemDto;
import com.airguardnet.device.domain.model.Device;

public class DeviceMapper {

    public DeviceListItemDto toListItem(Device device) {
        DeviceListItemDto dto = new DeviceListItemDto();
        dto.setId(device.getId());
        dto.setName(device.getName());
        dto.setStatus(device.getStatus());
        dto.setLastBatteryLevel(device.getLastBatteryLevel());
        return dto;
    }
}
