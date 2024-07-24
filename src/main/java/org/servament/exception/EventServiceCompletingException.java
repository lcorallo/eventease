package org.servament.exception;

import java.util.UUID;

public class EventServiceCompletingException extends EventEaseException {
    
    public EventServiceCompletingException(UUID id) {
        super(String.format("Event Service with the following UUID: %s, cannot be completed because is not in coming", id));
    }

    public EventServiceCompletingException(Throwable cause) {
        super(cause);
    }

    public EventServiceCompletingException(UUID id, Throwable cause) {
        super(String.format("Event Service with the following UUID: %s, cannot be completed because is not in coming", id), cause);
    }

    @Override
    public String getErrorCode() {
        return "EVENT_SERVICE.COMPLETING_DENIED";
    }
    
}
