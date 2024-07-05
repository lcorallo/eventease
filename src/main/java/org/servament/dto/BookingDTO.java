package org.servament.dto;

import java.time.Instant;
import java.util.UUID;

import org.servament.model.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    private Long id;

    private UUID event;

    private String eventCode;

    private UUID supplier;

    private UUID consumer;

    private BookingStatus status;

    private Instant createdAt;

}
