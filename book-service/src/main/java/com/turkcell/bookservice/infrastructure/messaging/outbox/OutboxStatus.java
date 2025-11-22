package com.turkcell.bookservice.infrastructure.messaging.outbox;

public enum OutboxStatus {
    PENDING,
    PROCESSED,
    FAILED
}
