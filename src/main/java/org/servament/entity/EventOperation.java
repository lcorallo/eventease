package org.servament.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "event_operation")
public class EventOperation extends Event {

    @ManyToOne
    @JoinColumn(name = "event_service_id", referencedColumnName = "id", nullable = false)
    private EventService eventService;

    @Column(name = "operator")
    private UUID operatorId;

    @Column(name = "num_partecipants")
    private Integer partecipants;
    
}
