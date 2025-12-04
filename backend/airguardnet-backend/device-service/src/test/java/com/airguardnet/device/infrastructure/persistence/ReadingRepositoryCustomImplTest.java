package com.airguardnet.device.infrastructure.persistence;

import com.airguardnet.device.domain.model.Reading;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReadingRepositoryCustomImplTest {

    @Test
    // Nro 59: Consulta de lecturas en rango de fechas excluye fuera de rango
    void findByDeviceAndRange_excludesOutOfRange() {
        Instant now = Instant.now();
        Instant from = now.minusSeconds(60);
        Instant to = now.plusSeconds(60);
        List<Reading> readings = List.of(
                Reading.builder().deviceId(1L).recordedAt(now.minusSeconds(10)).build(),
                Reading.builder().deviceId(1L).recordedAt(now.plusSeconds(10)).build(),
                Reading.builder().deviceId(1L).recordedAt(now.plusSeconds(120)).build()
        );
        ReadingRepositoryCustomImpl repository = new ReadingRepositoryCustomImpl(readings);

        List<Reading> result = repository.findByDeviceAndRange(1L, from, to);

        assertTrue(result.stream().allMatch(r -> !r.getRecordedAt().isBefore(from) && !r.getRecordedAt().isAfter(to)));
    }
}
