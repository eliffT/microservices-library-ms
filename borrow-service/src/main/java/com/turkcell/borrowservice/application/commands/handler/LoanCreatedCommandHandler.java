package com.turkcell.borrowservice.application.commands.handler;


import com.turkcell.borrowservice.application.commands.BorrowBookCommand;
import com.turkcell.borrowservice.application.exceptions.BusinessException;
import com.turkcell.borrowservice.application.ports.BookReadModelRepository;
import com.turkcell.borrowservice.application.ports.output.eventproducer.KafkaEventProducerPort;
import com.turkcell.borrowservice.domain.model.Loan;
import com.turkcell.borrowservice.domain.model.enumstatus.LoanStatus;
import com.turkcell.borrowservice.domain.repository.FineRepository;
import com.turkcell.borrowservice.domain.repository.LoanRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LoanCreatedCommandHandler {
    private final LoanRepository loanRepository;
    private final FineRepository fineRepository;
    private final BookReadModelRepository bookReadModelRepository;
    private final KafkaEventProducerPort eventPublisher;

    public LoanCreatedCommandHandler(LoanRepository loanRepository,
                                     FineRepository fineRepository,
                                     BookReadModelRepository bookReadModelRepository,
                                     KafkaEventProducerPort  eventPublisher) {
        this.loanRepository = loanRepository;
        this.fineRepository = fineRepository;
        this.bookReadModelRepository = bookReadModelRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public UUID handle(BorrowBookCommand command) {

        // Üyenin açık cezası varsa ödünç alamaz.
        if (fineRepository.existsByUserIdAndIsPaid(command.userId(), false))
            throw new BusinessException("The user has an unpaid fine.");

        // Aynı üyenin aynı kitabı aynı anda 2 kez ödünç alması yasak.
        if (loanRepository.findByUserIdAndBookIdAndStatus(command.userId(), command.bookId(), LoanStatus.BORROWED).isPresent())
            throw new BusinessException("The user has already borrowed this book.");

        // Kitap mevcut mu? (Book Service'ten gelen lokal Read Model verisi kullanılır)
        // Eğer bu kontrolü yapmak için Read Model'inizde veri yoksa, burası geçici olarak başarısız olur.
        if (!bookReadModelRepository.isBookAvailable(command.bookId()))
            throw new BusinessException("The book is currently unavailable for borrowing.");

        Loan loan = Loan.create(command.userId(), command.bookId(), command.loanDays());
        Loan savedLoan = loanRepository.save(loan);

        // Outbox'a kaydetme işi artık portun implementasyonunda (KafkaProducerAdapter) gizlidir.
        eventPublisher.publish(loan.getDomainEvents());
        loan.clearDomainEvents();   // Olaylar yayınlandıktan/kaydedildikten sonra temizlenir.

        return savedLoan.id().value();
    }


}
