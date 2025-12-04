package com.turkcell.common.events;

import java.time.OffsetDateTime;
import java.util.UUID;

public record BookDeletedEvent(
        UUID eventId,
        OffsetDateTime eventTime,
        UUID bookId

) implements DomainEvent {

    public BookDeletedEvent ( UUID bookId) {
        this(UUID.randomUUID(), OffsetDateTime.now(), bookId);
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public OffsetDateTime getEventTime() {
        return OffsetDateTime.now();
    }

    @Override
    public UUID getAggregateId() {
        return bookId;
    }

    @Override
    public AggregateType getAggregateType() {
        return AggregateType.BOOK;
    }
}