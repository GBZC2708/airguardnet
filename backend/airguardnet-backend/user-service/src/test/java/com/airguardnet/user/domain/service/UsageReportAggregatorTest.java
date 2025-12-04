package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.model.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsageReportAggregatorTest {

    @Test
    // Nro 36: Validar que usuarios activos cuentan para KPI de usuarios activos
    void countActiveUsers_returnsFour() {
        List<User> users = Arrays.asList(
                User.builder().status("ACTIVE").build(),
                User.builder().status("ACTIVE").build(),
                User.builder().status("ACTIVE").build(),
                User.builder().status("ACTIVE").build(),
                User.builder().status("DISABLED").build()
        );
        UsageReportAggregator aggregator = new UsageReportAggregator();
        assertEquals(4L, aggregator.countActiveUsers(users));
    }

    @Test
    // Nro 37: Calcular total de dispositivos en reportes
    void countDevices_returnsThree() {
        List<String> devices = Arrays.asList("d1", "d2", "d3");
        UsageReportAggregator aggregator = new UsageReportAggregator();
        assertEquals(3L, aggregator.countDevices(devices));
    }

    @Test
    // Nro 38: Calcular promedio de PM2.5 en periodo
    void averagePm25_calculatesAverage() {
        UsageReportAggregator.Pm25Provider r1 = () -> 30.0;
        UsageReportAggregator.Pm25Provider r2 = () -> 40.0;
        UsageReportAggregator.Pm25Provider r3 = () -> 60.0;
        List<UsageReportAggregator.Pm25Provider> readings = Arrays.asList(r1, r2, r3);

        UsageReportAggregator aggregator = new UsageReportAggregator();
        assertEquals(43.33, aggregator.averagePm25(readings), 0.1);
    }
}
