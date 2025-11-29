package com.airguardnet.device.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "usage_reports")
public class UsageReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "generated_at", nullable = false)
    private OffsetDateTime generatedAt;

    @Column(name = "total_users", nullable = false)
    private int totalUsers;

    @Column(name = "total_devices", nullable = false)
    private int totalDevices;

    @Column(name = "total_readings", nullable = false)
    private int totalReadings;

    @Column(name = "total_alerts", nullable = false)
    private int totalAlerts;

    // datos_resumen JSON/text
    @Column(name = "datos_resumen", columnDefinition = "text")
    private String datosResumen;

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(OffsetDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public int getTotalDevices() {
        return totalDevices;
    }

    public void setTotalDevices(int totalDevices) {
        this.totalDevices = totalDevices;
    }

    public int getTotalReadings() {
        return totalReadings;
    }

    public void setTotalReadings(int totalReadings) {
        this.totalReadings = totalReadings;
    }

    public int getTotalAlerts() {
        return totalAlerts;
    }

    public void setTotalAlerts(int totalAlerts) {
        this.totalAlerts = totalAlerts;
    }

    public String getDatosResumen() {
        return datosResumen;
    }

    public void setDatosResumen(String datosResumen) {
        this.datosResumen = datosResumen;
    }
}

