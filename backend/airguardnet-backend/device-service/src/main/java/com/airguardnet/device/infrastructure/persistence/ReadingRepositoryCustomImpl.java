package com.airguardnet.device.infrastructure.persistence;

import com.airguardnet.device.domain.model.Reading;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReadingRepositoryCustomImpl {

    private final List<Reading> data;

    public ReadingRepositoryCustomImpl(List<Reading> data) {
        this.data = new ArrayList<>(data);
    }

    public List<Reading> findByDeviceAndRange(Long deviceId, Instant from, Instant to) {
        return data.stream()
                .filter(r -> deviceId.equals(r.getDeviceId()))
                .filter(r -> !r.getRecordedAt().isBefore(from) && !r.getRecordedAt().isAfter(to))
                .collect(Collectors.toList());
    }
}
