package org.servament.exception.eventoperation;

import java.util.UUID;

import org.servament.exception.EventEaseException;

public class EventOperationNotFoundException extends EventEaseException {
    
    public EventOperationNotFoundException(UUID id) {
        super(String.format("Does not exists any event operation with the following UUID: %s", id));
    }

    public EventOperationNotFoundException(Throwable cause) {
        super(cause);
    }

    public EventOperationNotFoundException(UUID id, Throwable cause) {
        super(String.format("Does not exists any event operation with the following UUID: %s", id), cause);
    }

    @Override
    public String getErrorCode() {
        return "EVENT_OPERATION.NOT_FOUND";
    }

}
