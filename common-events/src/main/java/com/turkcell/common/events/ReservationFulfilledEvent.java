package com.turkcell.common.events;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ReservationFulfilledEvent(
        UUID eventId,
        OffsetDateTime eventTime,
        UUID reservationId,
        UUID userId,
        UUID bookId,
        OffsetDateTime expireDate
) implements DomainEvent {

    public ReservationFulfilledEvent(UUID reservationId, UUID userId, UUID bookId, OffsetDateTime expireDate) {
        this(UUID.randomUUID(), OffsetDateTime.now(), reservationId, userId, bookId, expireDate);
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