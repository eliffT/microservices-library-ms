package com.turkcell.common.events;


import java.time.OffsetDateTime;
import java.util.UUID;

public interface DomainEvent {
    UUID getEventId();

    OffsetDateTime getEventTime();

    // Olayın hangi aggregate (Loan/Reservation) ile ilgili olduğunu belirtir.
    UUID getAggregateId();

    // Hangi aggregate'a ait?
    AggregateType aggregateType();
}
