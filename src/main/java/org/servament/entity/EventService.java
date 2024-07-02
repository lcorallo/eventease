package org.servament.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity(name = "event_service")
public class EventService extends Event {

    @OneToMany(mappedBy = "eventService", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventOperation> operations;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    @Column(name = "code", unique = true, length = 100)
    private String code;

    @Column(name = "supplier")
    private UUID supplierId;

    @Column(name = "num_availability")
    private Integer availabilty;    
}
