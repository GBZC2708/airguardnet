package com.airguardnet.user.infrastructure.web;

import com.airguardnet.user.domain.model.Plan;

public class PlanDTO {
    private Long id;
    private String name;
    private String description;

    public static PlanDTO fromDomain(Plan plan) {
        PlanDTO dto = new PlanDTO();
        dto.id = plan.getId();
        dto.name = plan.getName();
        dto.description = plan.getDescription();
        return dto;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}
