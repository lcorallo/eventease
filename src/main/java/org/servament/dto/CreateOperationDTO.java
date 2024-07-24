package org.servament.dto;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOperationDTO {

    @NotNull
    private UUID event;

    @NotNull
    private UUID activity;

    @Future
    private Instant startDateTime;

    @Future
    private Instant estimatedEndDateTime;

    private UUID location;

    @NotNull
    private UUID operator;

    private Integer partecipants;
}
