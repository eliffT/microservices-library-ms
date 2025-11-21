package com.turkcell.common.events;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record FineCreatedEvent(
        UUID eventId,
        OffsetDateTime eventTime,
        UUID fineId,
        UUID userId,
        UUID loanId,
        BigDecimal amount) implements DomainEvent {

    public FineCreatedEvent(UUID fineId, UUID userId, UUID loanId, BigDecimal amount) {
        this(UUID.randomUUID(), OffsetDateTime.now(), fineId, userId, loanId, amount);
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
        return fineId;
    }

    @Override
    public AggregateType getAggregateType() {
        return AggregateType.FINE;
    }
}