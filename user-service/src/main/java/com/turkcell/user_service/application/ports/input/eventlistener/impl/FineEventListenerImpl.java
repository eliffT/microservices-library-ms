package com.turkcell.user_service.application.ports.input.eventlistener.impl;

import com.turkcell.common.events.FineCreatedEvent;
import com.turkcell.common.events.FinePaidEvent;
import com.turkcell.user_service.application.ports.input.eventlistener.FineEventListener;
import com.turkcell.user_service.infrastructure.persistence.entity.MembershipLevel;
import com.turkcell.user_service.infrastructure.persistence.entity.User;
import com.turkcell.user_service.infrastructure.persistence.entity.UserFine;
import com.turkcell.user_service.infrastructure.persistence.repository.UserFineRepository;
import com.turkcell.user_service.infrastructure.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

// Bu service, Kafka Consumer tarafından çağrıldığı için transactional olmalıdır.
@Service
public class FineEventListenerImpl implements FineEventListener {
    private final UserFineRepository userFineRepository;
    private final UserRepository userRepository;

    public FineEventListenerImpl(UserFineRepository userFineRepository, UserRepository userRepository) {
        this.userFineRepository = userFineRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void handleFineCreated(FineCreatedEvent event) {
        UserFine userFine = new UserFine(event);
        userFineRepository.save(userFine);

        User user = userRepository.findById(event.userId())
                .orElseThrow(() -> new RuntimeException("User not found: " + event.userId()));

        if (user.getMembershipLevel() != MembershipLevel.BANNED) {
            user.setMembershipLevel(MembershipLevel.BANNED);
            userRepository.save(user);
            System.out.println("ALERT: User " + event.userId() + " has been BANNED due to a new fine.");
        }
    }

    @Override
    @Transactional
    public void handleFinePaid(FinePaidEvent event) {
        // İlgili UserFine kaydını bul ve ödenmiş olarak işaretle
        UserFine fineToUpdate = userFineRepository.findById(event.fineId())
                .orElseThrow(() -> new RuntimeException("Fine ID not found in projection: " + event.fineId()));

        fineToUpdate.setPaid(true);
        userFineRepository.save(fineToUpdate);

        List<UserFine> unpaidFines = userFineRepository.findAllByUserIdAndIsPaidFalse(fineToUpdate.userId());
        if (unpaidFines.isEmpty()) {
            userRepository.findById(fineToUpdate.userId()).ifPresent(user -> {
                user.setMembershipLevel(MembershipLevel.STANDARD);
                userRepository.save(user);
            });

            System.out.println("INFO: User is no longer BANNED.");
        }
    }
}
