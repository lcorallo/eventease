package org.servament.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.servament.model.EventStatus;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor

@MappedSuperclass
public class Event extends PanacheEntityBase {

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
    @Column(name = "created_at")
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(name = "update_at")
    private Instant updateAt; 
}