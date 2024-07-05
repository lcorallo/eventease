package org.servament.model.filter;

import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor

public class EventServiceFilter extends EventFIlter {

    private Set<String> codes;

    private Set<UUID> suppliers;

}
