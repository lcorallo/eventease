package org.servament.dto;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDTO {
    
    @NotNull
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

    @NotBlank
    private String code;

    @NotNull
    private UUID supplier;

    private Integer availability;
}
