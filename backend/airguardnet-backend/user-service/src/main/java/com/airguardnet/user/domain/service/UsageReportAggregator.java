package com.airguardnet.user.domain.service;

import com.airguardnet.user.domain.model.User;

import java.util.List;
import java.util.OptionalDouble;

public class UsageReportAggregator {

    public long countActiveUsers(List<User> users) {
        return users.stream().filter(u -> "ACTIVE".equals(u.getStatus())).count();
    }

    public long countDevices(List<?> devices) {
        return devices.size();
    }

    public double averagePm25(List<? extends Pm25Provider> readings) {
        OptionalDouble average = readings.stream().mapToDouble(Pm25Provider::getPm25).average();
        return average.orElse(0.0);
    }

    public interface Pm25Provider {
        double getPm25();
    }
}
