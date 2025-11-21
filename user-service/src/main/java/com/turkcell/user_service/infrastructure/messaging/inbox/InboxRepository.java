package com.turkcell.user_service.infrastructure.messaging.inbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InboxRepository extends JpaRepository<InboxMessage, UUID> {
    // 'DataIntegrityViolationException' fırlatır, bu da idempotency kontrolünü sağlar.
}