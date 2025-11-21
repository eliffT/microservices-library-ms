package com.turkcell.user_service.application.ports.input.eventlistener.impl;

import com.turkcell.common.events.FineCreatedEvent;
import com.turkcell.common.events.FinePaidEvent;
import com.turkcell.user_service.application.ports.input.eventlistener.FineEventListener;
import com.turkcell.user_service.infrastructure.persistence.entity.UserFine;
import com.turkcell.user_service.infrastructure.persistence.repository.UserFineRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

// Bu service, Kafka Consumer tarafından çağrıldığı için transactional olmalıdır.
@Service
public class FineEventListenerImpl implements FineEventListener {
    private final UserFineRepository userFineRepository;

    public FineEventListenerImpl(UserFineRepository userFineRepository) {
        this.userFineRepository = userFineRepository;
    }

    @Override
    @Transactional
    public void handleFineCreated(FineCreatedEvent event) {
        UserFine userFine = new UserFine(event);
        userFineRepository.save(userFine);

        System.out.println("LOG: FineCreatedEvent işlendi. Yeni ceza projeksiyona kaydedildi: " + event.fineId());
    }

    @Override
    @Transactional
    public void handleFinePaid(FinePaidEvent event) {
        // İlgili UserFine kaydını bul ve ödenmiş olarak işaretle
        UserFine fineToUpdate = userFineRepository.findById(event.fineId())
                .orElseThrow(() -> new RuntimeException("Fine ID not found in projection: " + event.fineId()));

        fineToUpdate.setPaid(true);
        userFineRepository.save(fineToUpdate);

        System.out.println("LOG: FinePaidEvent işlendi. Ceza ödendi olarak güncellendi: " + event.fineId());
    }
}
