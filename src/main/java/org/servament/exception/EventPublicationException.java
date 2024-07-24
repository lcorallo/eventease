package org.servament.exception;

public class EventPublicationException extends EventEaseException {

    public EventPublicationException(String message) {
        super(message);
    }

    public EventPublicationException(Throwable cause) {
        super(cause);
    }

    public EventPublicationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getErrorCode() {
        return "EVENT.PUBLICATION_DENIED";
    }
}
