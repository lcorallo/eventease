package org.servament.exception.eventservice;

import java.util.UUID;

import org.servament.exception.EventEaseException;

public class EventServiceNotFoundException extends EventEaseException {

    public EventServiceNotFoundException(String code) {
        super(String.format("Does not exists any event service with the following code: %s", code));
    }

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
