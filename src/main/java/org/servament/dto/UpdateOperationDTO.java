package org.servament.dto;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOperationDTO {

    private UUID event;

    private UUID activity;

    @Future
    private Instant startDateTime;

    @Future
    private Instant estimatedEndDateTime;

    private UUID location;

    private UUID operator;

    private Integer partecipants;
}
