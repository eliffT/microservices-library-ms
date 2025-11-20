package com.turkcell.common.events;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ReservationCancelledEvent (
        UUID eventId,
        OffsetDateTime eventTime,
        UUID reservationId,
        UUID bookId,
        String reason // "Expired", "Manual"
) implements DomainEvent {

    public ReservationCancelledEvent (UUID reservationId, UUID bookId, String reason) {
        this(UUID.randomUUID(), OffsetDateTime.now(),reservationId, bookId, reason);
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
    public AggregateType getAggregateType() {
        return AggregateType.RESERVATION;
    }
}
