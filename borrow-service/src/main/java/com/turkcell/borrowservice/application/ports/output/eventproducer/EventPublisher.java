package com.turkcell.borrowservice.application.ports.output.eventproducer;

import com.turkcell.common.events.DomainEvent;

import java.util.List;

// Event gönderme işlevini soyutlayan Port
public interface EventPublisher {
    void publish(List<DomainEvent> events);

}
