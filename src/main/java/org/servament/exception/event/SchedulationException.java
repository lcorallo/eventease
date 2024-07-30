package org.servament.exception.event;

import org.servament.exception.EventEaseException;

public class SchedulationException extends EventEaseException {

    public SchedulationException(Throwable cause) {
        super("Failed to execute the schedulation of events", cause);
    }

    @Override
    public String getErrorCode() {
        return "EVENT.SCHEDULATION";
    }
    
}