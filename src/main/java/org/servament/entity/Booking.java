package org.servament.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.servament.model.BookingStatus;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "booking")
public class Booking extends PanacheEntityBase{
    
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_service_id", referencedColumnName = "id", nullable = false)
    private EventService event;

    @Column(name = "consumer")
    private UUID consumerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;
    
    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updateAt;
}
