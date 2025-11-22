package com.turkcell.bookservice.infrastructure.messaging.inbox;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

// Loan olaylarını işlerken , InboxRepository'nin eventid bazında Unique Constraint Violation fırlatması,
// idempotency'yi (aynı mesajın birden fazla kez gelmesi durumunda bile tek bir kez işlenmesini) sağlar.

@Entity
@Table(name = "inbox", indexes = {
        @Index(name = "ix_inbox_aggregate_id", columnList = "aggregateId")
})
public class InboxMessage {

    // Olayın Benzersiz ID'si aynı zamanda Birincil Anahtardır (PK).
    // Bu, aynı olayın iki kez kaydedilmesini önleyen UNIQUE kısıtlamasını sağlar.
    @Id
    @Column(nullable = false, columnDefinition = "uuid")
    private UUID eventId;

    private String aggregateType; // BOOK, LOAN, etc.
    @Column(columnDefinition = "uuid")
    private UUID aggregateId; // Etkilenen Aggregate Root'un ID'si (örn. Book ID)

    @Enumerated(EnumType.STRING)
    private InboxStatus status = InboxStatus.PROCESSED;

    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime processedAt = OffsetDateTime.now();

    public InboxMessage() {}

    public InboxMessage(UUID eventId, UUID aggregateId, String aggregateType) {
        this.eventId = eventId;
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
    }

    // Getters and Setters
    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }

    public String getAggregateType() { return aggregateType; }
    public void setAggregateType(String aggregateType) { this.aggregateType = aggregateType; }

    public UUID getAggregateId() { return aggregateId; }
    public void setAggregateId(UUID aggregateId) { this.aggregateId = aggregateId; }

    public InboxStatus getStatus() { return status; }
    public void setStatus(InboxStatus status) { this.status = status; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(OffsetDateTime processedAt) { this.processedAt = processedAt; }
}