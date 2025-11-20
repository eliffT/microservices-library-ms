package com.turkcell.borrowservice.infrastructure.messaging.inbox;

public enum InboxStatus {
    PROCESSED,  // Başarıyla işlendi
    FAILED      // İşlenemedi (DLQ/manuel müdahale gerektiren hata)
}
