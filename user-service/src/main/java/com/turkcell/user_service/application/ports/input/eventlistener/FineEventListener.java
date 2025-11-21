package com.turkcell.user_service.application.ports.input.eventlistener;

import com.turkcell.common.events.FineCreatedEvent;
import com.turkcell.common.events.FinePaidEvent;

public interface FineEventListener {
    /**
     * Yeni ceza oluşturma olayını işler ve UserFine projeksiyonuna kayıt yapar.
     */
    void handleFineCreated(FineCreatedEvent event);

    /**
     * Ceza ödeme olayını işler ve UserFine projeksiyonunda isPaid alanını günceller.
     */
    void handleFinePaid(FinePaidEvent event);
}