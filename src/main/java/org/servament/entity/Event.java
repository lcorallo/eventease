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

    @Column(name = "activity", nullable = false)
    private UUID activity;

    @Column(name = "start_datetime", nullable = false)
    private Instant startDateTime;

    @Column(name = "estimated_end_datetime", nullable = false)
    private Instant estimatedEndDateTime;

    @Column(name = "end_datetime", nullable = true)
    private Instant endDateTime;
    
    @Column(name = "start_booking_datetime", nullable = true)
    private Instant startBookingDateTime;

    @Column(name = "end_booking_datetime", nullable = true)
    private Instant endBookingDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EventStatus status;

    @Column(name = "note", length = 1000, nullable = true)
    private String note;

    @Column(name = "location", nullable = true)
    private UUID location;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(name = "update_at")
    private Instant updateAt; 
}