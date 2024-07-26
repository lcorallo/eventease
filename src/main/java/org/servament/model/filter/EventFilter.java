package org.servament.model.filter;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import org.servament.model.EventStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor

public abstract class EventFilter extends BaseFilter {

    private Instant fromStartDate;

    private Instant endStartDate;

    private Set<EventStatus> statuses;

    private Set<UUID> activities;

}
