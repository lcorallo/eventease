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
public class EventOperationFilter extends EventFIlter {

    private List<UUID> eventServiceIds;
    
    private List<UUID> operators;

}
