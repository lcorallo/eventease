package org.servament.exception;

import java.util.UUID;

public class EventServiceClosingException extends EventEaseException {
    
    public EventServiceClosingException(UUID id) {
        super(String.format("Event Service with the following UUID: %s, cannot be closed because is not in coming", id));
    }

    public EventServiceClosingException(Throwable cause) {
        super(cause);
    }

    public EventServiceClosingException(UUID id, Throwable cause) {
        super(String.format("Event Service with the following UUID: %s, cannot be closed because is not in coming", id), cause);
    }

    @Override
    public String getErrorCode() {
        return "EVENT_SERVICE.CLOSING_DENIED";
    }
    
}
