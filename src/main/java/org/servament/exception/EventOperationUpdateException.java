package org.servament.exception;

import java.util.UUID;

public class EventOperationUpdateException extends EventEaseException {
    
    public EventOperationUpdateException(UUID id) {
        super(String.format("An unknown error occur during update of the entity with th following UUID: %s", id));
    }

    public EventOperationUpdateException(Throwable cause) {
        super(cause);
    }

    public EventOperationUpdateException(UUID id, Throwable cause) {
        super(String.format("An unknown error occur during update of the entity with th following UUID: %s", id), cause);
    }

    @Override
    public String getErrorCode() {
        return "EVENT_OPERATION.UPDATE_FAILURE";
    }

}
