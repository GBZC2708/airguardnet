package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.model.User;
import com.airguardnet.user.domain.model.UserSummaryReport;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserReportServiceTest {

    private final UserReportService userReportService = new UserReportService(new UsageReportAggregator());

    @Test
    // Nro 63: Generar reporte resumido para usuario final
    void buildUserSummary_setsTotalsAndAverage() {
        User user = User.builder().id(5L).build();
        List<UsageReportAggregator.Pm25Provider> readings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            double value = i + 1;
            readings.add(() -> value);
        }

        UserSummaryReport report = userReportService.buildUserSummary(user, readings);

        assertEquals(user.getId(), report.getUserId());
        assertEquals(10, report.getTotalReadings());
    }
}
