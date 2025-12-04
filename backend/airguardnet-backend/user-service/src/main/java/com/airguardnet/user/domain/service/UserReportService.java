package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.model.User;
import com.airguardnet.user.domain.model.UserSummaryReport;

import java.util.List;

public class UserReportService {

    private final UsageReportAggregator usageReportAggregator;

    public UserReportService() {
        this(new UsageReportAggregator());
    }

    public UserReportService(UsageReportAggregator usageReportAggregator) {
        this.usageReportAggregator = usageReportAggregator;
    }

    public UserSummaryReport buildUserSummary(User user, List<? extends UsageReportAggregator.Pm25Provider> readings) {
        UserSummaryReport report = new UserSummaryReport();
        report.setUserId(user.getId());
        report.setTotalReadings(readings.size());
        report.setAvgPm25(usageReportAggregator.averagePm25(readings));
        return report;
    }
}
