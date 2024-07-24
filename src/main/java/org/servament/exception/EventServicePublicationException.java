package org.servament.exception;

public class EventServicePublicationException extends EventEaseException {

    public EventServicePublicationException(String message) {
        super(message);
    }

    public EventServicePublicationException(Throwable cause) {
        super(cause);
    }

    public EventServicePublicationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getErrorCode() {
        return "EVENT_SERVICE.PUBLICATION_DENIED";
    }
}
