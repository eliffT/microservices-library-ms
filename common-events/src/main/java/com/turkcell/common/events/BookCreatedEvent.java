package com.turkcell.common.events;

import java.time.OffsetDateTime;
import java.util.UUID;

public record BookCreatedEvent(
        UUID eventId,
        OffsetDateTime eventTime,
        UUID bookId,           // Aggregate Root ID'si
        String isbn,
        String title,
        UUID authorId,
        UUID publisherId,
        UUID categoryId,
        Integer initialStockCount
) implements DomainEvent {

    public BookCreatedEvent ( UUID bookId, String isbn, String title, UUID authorId,  UUID publisherId, UUID categoryId, Integer initialStockCount) {
        this(UUID.randomUUID(), OffsetDateTime.now(), bookId, isbn, title, authorId, publisherId, categoryId, initialStockCount);
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public OffsetDateTime getEventTime() {
        return eventTime;
    }

    @Override
    public UUID getAggregateId() {
        return bookId;
    }

    @Override
    public AggregateType aggregateType() {
        return AggregateType.BOOK;
    }
}