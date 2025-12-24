package com.turkcell.bookservice.application.ports.output.eventproducer;
import com.turkcell.common.events.DomainEvent;

import java.util.List;

// Verilen Domain Event'i dış mesajlaşma sistemine asenkron olarak yayımlar.

public interface EventPublisher {
    void publish(List<DomainEvent> event);
}