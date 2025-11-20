package com.turkcell.bookservice.infrastructure.messaging.inbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// LoanCreatedEvent'in birden fazla kez gelmesi durumunda, stokun yalnızca bir kez düşürülmesini garanti eder.

public interface InboxRepository extends JpaRepository<InboxMessage, UUID> {
    // 'DataIntegrityViolationException' fırlatır, bu da idempotency kontrolünü sağlar.
}