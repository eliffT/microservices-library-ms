package com.turkcell.borrowservice.application.commands.handler;

import com.turkcell.borrowservice.application.commands.ReturnBookCommand;
import com.turkcell.borrowservice.application.exceptions.NotFoundException;
import com.turkcell.borrowservice.application.ports.output.eventproducer.EventPublisher;
import com.turkcell.borrowservice.domain.model.DomainId;
import com.turkcell.borrowservice.domain.model.Fine;
import com.turkcell.borrowservice.domain.model.Loan;
import com.turkcell.borrowservice.domain.repository.FineRepository;
import com.turkcell.borrowservice.domain.repository.LoanRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReturnBookCommandHandler {
    private final LoanRepository loanRepository;
    private final FineRepository fineRepository;
    private final EventPublisher eventPublisher;


    public ReturnBookCommandHandler(LoanRepository loanRepository,
                                    FineRepository fineRepository,
                                    EventPublisher eventPublisher) {
        this.loanRepository = loanRepository;
        this.fineRepository = fineRepository;
       this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void handle(ReturnBookCommand command) {
        Loan loan = loanRepository.findById(new DomainId<>(command.loanId()))
                .orElseThrow(() -> new NotFoundException("Loan not found with ID: " + command.loanId()));

        // DOMAIN ÇAĞRISI (Loan Durumunu İadeye Çevir)
        // Bu çağrı içinde LoanReturnedEvent oluşturulur ve kaydedilir.
        loan.markAsReturned();

        loanRepository.save(loan);

        Optional<Fine> optionalFine = loan.calculateLateFine();

        if (optionalFine.isPresent()) {
            Fine fine = optionalFine.get();
            // Fine Aggregate'i kaydedilir
            fineRepository.save(fine);
            // FINE OUTBOX KAYDI
            eventPublisher.publish(fine.getDomainEvents());
            fine.clearDomainEvents();
        }
        // LOAN OUTBOX KAYDI
        eventPublisher.publish(loan.getDomainEvents());
        loan.clearDomainEvents();
    }
}
