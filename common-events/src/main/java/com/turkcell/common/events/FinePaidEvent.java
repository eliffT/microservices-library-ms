package com.turkcell.common.events;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record FinePaidEvent (
        UUID eventId,
        OffsetDateTime eventTime,
        UUID fineId,
        UUID userId,
        BigDecimal amountPaid,
        OffsetDateTime paymentDate
) implements DomainEvent {

    public FinePaidEvent(UUID fineId, UUID userId, BigDecimal amountPaid, OffsetDateTime paymentDate) {
        this(UUID.randomUUID(), OffsetDateTime.now(), fineId, userId, amountPaid, paymentDate);
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public OffsetDateTime getEventTime() {
        return eventTime;
    }

    // Üye durumu ile ilgili bir olaydır
    @Override
    public UUID getAggregateId() {
        return fineId;
    }

    @Override
    public AggregateType getAggregateType() {
        return AggregateType.FINE;
    }
}