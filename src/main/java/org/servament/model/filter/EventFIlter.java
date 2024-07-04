package org.servament.model.filter;

import java.util.List;

import org.servament.model.EventStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor

public abstract class EventFIlter {

    private List<EventStatus> statuses;

}
