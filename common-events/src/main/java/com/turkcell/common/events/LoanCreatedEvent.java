package com.turkcell.common.events;

import java.time.OffsetDateTime;
import java.util.UUID;

public record LoanCreatedEvent (
        UUID eventId,
        OffsetDateTime eventTime,
        UUID loanId,
        UUID userId,
        UUID bookId,
        OffsetDateTime borrowDate,
        OffsetDateTime dueDate) implements DomainEvent {

    public LoanCreatedEvent(UUID loanId, UUID userId, UUID bookId, OffsetDateTime borrowDate, OffsetDateTime dueDate) {
        this(UUID.randomUUID(), OffsetDateTime.now(), loanId, userId, bookId, borrowDate, dueDate);
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public OffsetDateTime getEventTime() {
        return eventTime;
    }

    // Bu olay Loan Aggregate'i ile ilişkilidir.
    @Override
    public UUID getAggregateId() {
        return loanId;
    }

    @Override
    public AggregateType aggregateType() {
        return AggregateType.LOAN;
    }
}
