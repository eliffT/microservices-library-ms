package com.turkcell.borrowservice.domain.model;

import com.turkcell.borrowservice.domain.event.LoanCreatedEvent;
import com.turkcell.borrowservice.domain.event.LoanReturnedEvent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

public class Loan {

    private final DomainId<Loan> id;
    private final UUID userId;
    private  final UUID bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus status;
    private Object domainEvent;


    // Sabit günlük gecikme ücreti
    private static final BigDecimal DAILY_LATE_FEE = new BigDecimal("15");


    private Loan(DomainId<Loan> id, UUID userId, UUID bookId, LocalDate borrowDate, LocalDate dueDate,
                 LoanStatus status) {

        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public static Loan create(UUID userId, UUID bookId,  int loanDays){

        LocalDate borrowDate = LocalDate.now();
        if (loanDays <= 0) throw new IllegalArgumentException("Loan days must be greater than 0");
        LocalDate dueDate = borrowDate.plusDays(loanDays);

        Loan loan = new Loan(DomainId.generate(), userId, bookId,  borrowDate, dueDate, LoanStatus.BORROWED);
        loan.domainEvent = new LoanCreatedEvent(
                loan.id.value(),
                loan.userId,
                loan.bookId,
                borrowDate,
                dueDate);
        return loan;
    }

    public static Loan rehydrate(DomainId<Loan> id,
                                 UUID userId,
                                 UUID bookId,
                                 LocalDate borrowDate,
                                 LocalDate dueDate,
                                 LocalDate returnDate,
                                 LoanStatus status) {

        Loan loan = new Loan(id, userId, bookId, borrowDate, dueDate, status);
        if (status == LoanStatus.RETURNED && returnDate != null)
            loan.returnDate = returnDate;
        return loan;
    }


    public Optional<Fine> calculateLateFine() {
        LocalDate endDate = returnDate != null ? returnDate : LocalDate.now();
        if(endDate.isAfter(dueDate)) {
            int daysLate = (int) ChronoUnit.DAYS.between(dueDate, endDate);
            return Optional.of(Fine.forLate(daysLate, DAILY_LATE_FEE));
        }
        return Optional.empty(); // gecikme yok
    }

    private boolean isActive() {
        return status == LoanStatus.BORROWED || status == LoanStatus.LATE;
    }

    public void markAsReturned() {
        if(!isActive()) throw new IllegalStateException("Only borrowed or late loans can be returned");
        this.status = LoanStatus.RETURNED;
        this.returnDate = LocalDate.now();

        this.domainEvent = new LoanReturnedEvent(
                id.value(),
                userId,
                bookId,
                returnDate
        );
    }

    // Ödünç alınan kitapların teslim tarihi kontrol edilir, gecikmiş ise LATE atanır
    public boolean checkAndMarkOverdue(LocalDate today) {
        if (status == LoanStatus.BORROWED && today.isAfter(dueDate)) {
            status = LoanStatus.LATE;
            return true;
        }
        return false; // Gecikme yoksa false
    }



    //Getters
    public DomainId<Loan> id() { return id; }
    public UUID userId() {return userId;}
    public LocalDate borrowDate() { return borrowDate; }
    public LocalDate dueDate() { return dueDate; }
    public LocalDate returnDate() { return returnDate; }
    public LoanStatus status() { return status; }
    public UUID bookId() {return bookId;}
    public Object domainEvent() {return domainEvent;}

}
