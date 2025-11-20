package com.turkcell.borrowservice.application.commands.handler;

import com.turkcell.borrowservice.application.commands.PayFineCommand;
import com.turkcell.borrowservice.application.exceptions.BusinessException;
import com.turkcell.borrowservice.application.exceptions.NotFoundException;
import com.turkcell.borrowservice.domain.model.DomainId;
import com.turkcell.borrowservice.domain.model.Fine;
import com.turkcell.borrowservice.domain.repository.FineRepository;
import com.turkcell.borrowservice.infrastructure.messaging.relayer.OutboxEventPersister;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class PayFineCommandHandler {
    private final FineRepository fineRepository;
    private final OutboxEventPersister outboxService;

    public PayFineCommandHandler(FineRepository fineRepository,
                                 OutboxEventPersister outboxService) {
        this.fineRepository = fineRepository;
        this.outboxService = outboxService;
    }

    @Transactional
    public void handle(PayFineCommand command) {

        Fine fine = fineRepository.findById(new DomainId<Fine>(command.fineId()))
                .orElseThrow(() -> new NotFoundException("Fine not found with ID: " + command.fineId()));

        // Domain kuralı: Ceza zaten ödenmiş mi?
        if (fine.isPaid()) {
            throw new BusinessException(" This fine has already been paid" + fine.id().value());
        }

        // Bu çağrı içinde FinePaidEvent tetiklenir ve kaydedilir.
        fine.markAsPaid();

        fineRepository.save(fine);
        outboxService.save(fine.getDomainEvents());
        fine.clearDomainEvents();
    }
}
