package org.servament.exception.event;

import java.util.UUID;

import org.servament.exception.EventEaseException;

public class EventClosingException extends EventEaseException {
    
    public EventClosingException(UUID id) {
        super(String.format("Event Service with the following UUID: %s, cannot be closed because is not in coming", id));
    }

    public EventClosingException(Throwable cause) {
        super(cause);
    }

    public EventClosingException(UUID id, Throwable cause) {
        super(String.format("Event Service with the following UUID: %s, cannot be closed because is not in coming", id), cause);
    }

    @Override
    public String getErrorCode() {
        return "EVENT.CLOSING_DENIED";
    }
    
}
