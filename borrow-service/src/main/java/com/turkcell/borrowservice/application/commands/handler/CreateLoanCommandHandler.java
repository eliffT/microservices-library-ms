package com.turkcell.borrowservice.application.commands.handler;


import com.turkcell.borrowservice.application.commands.BorrowBookCommand;
import com.turkcell.borrowservice.application.exceptions.BusinessException;
import com.turkcell.borrowservice.application.ports.BookReadModelRepository;
import com.turkcell.borrowservice.application.ports.output.eventproducer.EventPublisher;
import com.turkcell.borrowservice.application.ports.output.external.UserQueryPort;
import com.turkcell.borrowservice.domain.model.Loan;
import com.turkcell.borrowservice.domain.model.enumstatus.LoanStatus;
import com.turkcell.borrowservice.domain.repository.FineRepository;
import com.turkcell.borrowservice.domain.repository.LoanRepository;
import com.turkcell.borrowservice.domain.rules.LoanRuleSet;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CreateLoanCommandHandler {
    private final LoanRepository loanRepository;
    private final FineRepository fineRepository;
    private final EventPublisher eventPublisher;
    private final UserQueryPort userQueryPort;
    private final BookReadModelRepository bookReadModelRepository;

    public CreateLoanCommandHandler(LoanRepository loanRepository,
                                    FineRepository fineRepository,
                                    EventPublisher eventPublisher,
                                    UserQueryPort userQueryPort, BookReadModelRepository bookReadModelRepository) {
        this.loanRepository = loanRepository;
        this.fineRepository = fineRepository;
        this.eventPublisher = eventPublisher;
        this.userQueryPort = userQueryPort;
        this.bookReadModelRepository = bookReadModelRepository;
    }

    @Transactional
    public UUID handle(BorrowBookCommand command) {

        // 1. Üyelik Durumunu Çek ve Kural Setini Belirle (Tek Bir Çağrı)
        String memberStatus = userQueryPort.getMembershipLevel(command.userId());
        LoanRuleSet rules = LoanRuleSet.fromString(memberStatus);

        // 2. KRİTİK KURAL: BANNED KONTROLÜ
        if (rules == LoanRuleSet.BANNED) {
            throw new BusinessException("User ID: " + command.userId() + " is BANNED and cannot create a loan.");
        }

        // 3. KURAL: Ödenmemiş Ceza Kontrolü
        if (fineRepository.existsByUserIdAndIsPaid(command.userId(), false))
            throw new BusinessException("The user has an unpaid fine.");

        // 4. KURAL: Aynı Kitabı Tekrar Ödünç Alma Kontrolü
        if (loanRepository.findByUserIdAndBookIdAndStatus(command.userId(), command.bookId(), LoanStatus.BORROWED).isPresent())
            throw new BusinessException("The user has already borrowed this book.");

        // 5. KURAL: AÇIK ÖDÜNÇ LİMİTİ KONTROLÜ
        int allowedLimit = rules.maxLoans();
        long openLoanCount = loanRepository.countByUserIdAndStatusIn(
                command.userId(),
                List.of(LoanStatus.BORROWED, LoanStatus.LATE)
        );
        if (openLoanCount >= allowedLimit)
            throw new BusinessException("Kural İhlali: Üye, " + allowedLimit + " olan açık ödünç limitine ulaştı.");

        // 6. Stock kontrolü
        boolean isAvailable = bookReadModelRepository.isBookAvailable(command.bookId());
        int stock = bookReadModelRepository.getAvailableStock(command.bookId());

        if (!isAvailable || stock <= 0) {
            throw new RuntimeException("Kitap şu an ödünç verilebilir durumda değil (Stok yetersiz).");
        }

        // 7. LOAN OLUŞTURMA
        int loanDays = rules.loanDays(); // Enum'dan Gün sayısı çekildi
        Loan loan = Loan.create(command.userId(), command.bookId(), loanDays);
        Loan savedLoan = loanRepository.save(loan);

        // 8. EVENT YAYINLAMA
        // Outbox'a kaydetme işi artık portun implementasyonunda (KafkaProducerAdapter) gizlidir.
        eventPublisher.publish(loan.getDomainEvents());
        loan.clearDomainEvents();   // Olaylar yayınlandıktan/kaydedildikten sonra temizlenir.

        return savedLoan.id().value();
    }


}
