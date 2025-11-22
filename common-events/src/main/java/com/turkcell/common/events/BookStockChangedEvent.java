package com.turkcell.common.events;

import java.time.OffsetDateTime;
import java.util.UUID;

public record BookStockChangedEvent (
        UUID eventId,
        OffsetDateTime eventTime,
        UUID aggregateId, // bookId,
        int newAvailableStock, // Borrow Service'in lokal kopyasında ihtiyacı olan güncel stok
        String changeReason    // Stok değişikliğinin nedeni (Örn: "BookBorrowed", "BookReturned", "InventoryAdjustment")
) implements DomainEvent {

    public BookStockChangedEvent(UUID bookId, int newAvailableStock, String changeReason) {
        this(UUID.randomUUID(), OffsetDateTime.now(), bookId, newAvailableStock, changeReason);
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
        return aggregateId;
    }

    @Override
    public AggregateType getAggregateType() {
        return AggregateType.BOOK;
    }
}