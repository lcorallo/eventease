package org.servament.exception.eventservice;

import java.util.UUID;

import org.servament.exception.EventEaseException;

public class EventServiceUpdateException extends EventEaseException {
    
    public EventServiceUpdateException(UUID id) {
        super(String.format("An unknown error occur during update of the entity with th following UUID: %s", id));
    }

    public EventServiceUpdateException(Throwable cause) {
        super(cause);
    }

    public EventServiceUpdateException(UUID id, Throwable cause) {
        super(String.format("An unknown error occur during update of the entity with th following UUID: %s", id), cause);
    }

    @Override
    public String getErrorCode() {
        return "EVENT_SERVICE.UPDATE_FAILURE";
    }

}
