package org.servament.dto;

import java.time.Instant;
import java.util.UUID;

import org.servament.model.EventStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationDTO {

    private UUID id;

    private UUID event;

    private UUID activity;
    
    private Instant startDateTime;

    private Instant estimatedEndDateTime;

    private Instant endDateTime;

    private EventStatus status;

    private String note;

    private UUID location;

    private UUID operator;

    private Integer partecipants;
    
}
