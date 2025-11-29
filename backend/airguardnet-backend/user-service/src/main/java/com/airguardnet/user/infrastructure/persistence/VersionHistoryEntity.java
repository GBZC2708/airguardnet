package com.airguardnet.user.infrastructure.persistence;

import com.airguardnet.user.domain.model.VersionHistory;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "version_history")
@Data
public class VersionHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String versionNumber;
    @Column(length = 2000)
    private String description;
    private Instant releasedAt;

    public VersionHistory toDomain() {
        return VersionHistory.builder()
                .id(id)
                .versionNumber(versionNumber)
                .description(description)
                .releasedAt(releasedAt)
                .build();
    }
}
