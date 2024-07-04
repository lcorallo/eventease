package org.servament.model.filter;

import java.util.List;
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

    private List<String> codes;

    private List<UUID> suppliers;

}
