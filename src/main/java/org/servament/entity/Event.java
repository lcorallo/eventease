package org.servament.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.servament.model.EventStatus;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class Event extends PanacheEntityBase {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "activity")
    private UUID activityId;

    @Column(name = "start_datetime")
    private Instant startDateTime;

    @Column(name = "estimated_end_datetime")
    private Instant estimatedEndDateTIme;

    @Column(name = "end_datetime")
    private Instant endDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EventStatus status;

    @Column(name = "note", length = 1000)
    private String note;

    @Column(name = "location")
    private UUID locationId;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updateAt;
}