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
public class UpdateEventDTO {
    
    private UUID activity;

    @Future
    private Instant startDateTime;
    
    @Future
    private Instant estimatedEndDateTime;

    private UUID location;

    private String code;

    private UUID supplier;

    private Integer availability;
}
