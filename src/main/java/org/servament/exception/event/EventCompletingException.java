package org.servament.exception.event;

import java.util.UUID;

import org.servament.exception.EventEaseException;

public class EventCompletingException extends EventEaseException {
    
    public EventCompletingException(UUID id) {
        super(String.format("Event Service with the following UUID: %s, cannot be completed because is not in coming", id));
    }

    public EventCompletingException(Throwable cause) {
        super(cause);
    }

    public EventCompletingException(UUID id, Throwable cause) {
        super(String.format("Event Service with the following UUID: %s, cannot be completed because is not in coming", id), cause);
    }

    @Override
    public String getErrorCode() {
        return "EVENT.COMPLETING_DENIED";
    }
    
}
