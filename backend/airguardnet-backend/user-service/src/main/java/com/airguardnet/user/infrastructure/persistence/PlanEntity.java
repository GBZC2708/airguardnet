package com.airguardnet.user.infrastructure.persistence;

import com.airguardnet.user.domain.model.Plan;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "plans")
@Data
public class PlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 2000)
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    public Plan toDomain() {
        return Plan.builder()
                .id(id)
                .name(name)
                .description(description)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
