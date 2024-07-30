package org.servament.model;

public enum EventStatus {
    // Used to represent the status of an event that is in the draft stage and has not been published.
    DRAFT,

    // Used to represent the status of an event that is new and has not been booked.
    PUBLISHED,

    // Used to represent the status of an event that is in progress.
    IN_PROGRESS,

    // Used to represent the status of an event that has been completed.
    COMPLETED,

    // Used to represent the status of an event that has been cancelled.
    CANCELLED,

    // Used to represent the status of an event that has been closed.
    CLOSED
}