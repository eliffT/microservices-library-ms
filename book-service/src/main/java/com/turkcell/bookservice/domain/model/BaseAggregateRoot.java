package com.turkcell.bookservice.domain.model;


import com.turkcell.common.events.DomainEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseAggregateRoot {
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    // Protected Metot: Alt sınıfların olay kaydetmesi için
    protected void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    // Public Metot: Outbox Service'in olayları alması için
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    // Public Metot: Outbox'a kaydedildikten sonra olayları temizlemek için
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}
