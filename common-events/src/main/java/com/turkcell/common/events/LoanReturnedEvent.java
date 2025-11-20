package com.turkcell.common.events;

import java.time.OffsetDateTime;
import java.util.UUID;

public record LoanReturnedEvent (
    UUID eventId,
    OffsetDateTime eventTime,
    UUID loanId,
    UUID userId,
    UUID bookId,
    OffsetDateTime returnDate) implements DomainEvent{

    public LoanReturnedEvent(UUID loanId, UUID userId, UUID bookId, OffsetDateTime returnDate) {
            this(UUID.randomUUID(), OffsetDateTime.now(), loanId, userId, bookId,returnDate);
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
            return loanId;
        }

    @Override
    public AggregateType getAggregateType() {
        return AggregateType.LOAN;
    }
}
