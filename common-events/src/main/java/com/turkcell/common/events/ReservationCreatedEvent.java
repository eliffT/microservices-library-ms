package com.turkcell.common.events;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ReservationCreatedEvent(
        UUID eventId,
        OffsetDateTime eventTime,
        UUID reservationId,
        UUID userId,
        UUID bookId
) implements DomainEvent {

    public ReservationCreatedEvent(UUID reservationId, UUID userId, UUID bookId) {
        this(UUID.randomUUID(), OffsetDateTime.now(), reservationId, userId, bookId);
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
        return reservationId;
    }

    @Override
    public AggregateType aggregateType() {
        return AggregateType.RESERVATION;
    }

}