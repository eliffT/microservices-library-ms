package com.turkcell.bookservice.application.ports.output.eventproducer;
import com.turkcell.common.events.DomainEvent;

import java.util.List;

public interface EventPublisher {

//Verilen Domain Event'i dış mesajlaşma sistemine asenkron olarak yayımlar.
    void publish(List<DomainEvent> event);
}