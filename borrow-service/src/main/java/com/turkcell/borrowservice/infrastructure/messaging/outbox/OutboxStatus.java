package com.turkcell.borrowservice.infrastructure.messaging.outbox;

public enum OutboxStatus {
    PENDING,
    PROCESSED,
    FAILED
}
