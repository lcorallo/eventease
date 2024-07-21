package org.servament.model.filter;

import java.util.Set;

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

    private Set<EventStatus> statuses;

}
