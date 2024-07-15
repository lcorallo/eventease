package org.servament.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import org.servament.model.EventStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {

    private UUID id;

    private UUID activity;

    private Set<UUID> operations;

    private Instant startDateTime;

    private Instant estimatedEndDateTime;

    private Instant endDateTime;

    private EventStatus status;

    private String note;

    private UUID location;

    private String code;

    private UUID supplier;

    private Integer availability;
    
}
