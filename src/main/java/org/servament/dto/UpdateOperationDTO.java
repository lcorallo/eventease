package org.servament.dto;

import java.time.Instant;
import java.util.UUID;

import org.servament.model.EventStatus;
import org.servament.util.EnumNamePattern;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
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

    @Future
    private Instant endDateTime;

    @Size(max = 1000)
    private String note;

    private UUID location;

    private UUID operator;

    private Integer partecipants;

    @EnumNamePattern(regexp = "DRAFT|PUBLISHED|CANCELLED")
    private EventStatus status;
}
