package org.servament.exception;

import java.util.UUID;

public class EventServiceNotFoundException extends EventEaseException {

    public EventServiceNotFoundException(UUID id) {
        super(String.format("Does not exists any event service with the following UUID: %s", id));
    }

    public EventServiceNotFoundException(Throwable cause) {
        super(cause);
    }

    public EventServiceNotFoundException(UUID id, Throwable cause) {
        super(String.format("Does not exists any event service with the following UUID: %s", id), cause);
    }

    @Override
    public String getErrorCode() {
        return "EVENT_SERVICE.NOT_FOUND";
    }

}
