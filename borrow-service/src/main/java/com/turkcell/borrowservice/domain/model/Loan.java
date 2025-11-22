package com.turkcell.borrowservice.domain.model;

import com.turkcell.borrowservice.domain.model.enumstatus.LoanStatus;
import com.turkcell.common.events.LoanCreatedEvent;
import com.turkcell.common.events.LoanReturnedEvent;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Loan extends BaseAggregateRoot {

    private final DomainId<Loan> id;
    private final UUID userId;
    private  final UUID bookId;
    private OffsetDateTime borrowDate;
    private OffsetDateTime dueDate;
    private OffsetDateTime returnDate;
    private LoanStatus status;

    // Sabit günlük gecikme ücreti
    private static final BigDecimal DAILY_LATE_FEE = new BigDecimal("15");


    private Loan(DomainId<Loan> id, UUID userId, UUID bookId, OffsetDateTime borrowDate, OffsetDateTime dueDate,
                 LoanStatus status) {

        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public static Loan create(UUID userId, UUID bookId,  int loanDays){

        OffsetDateTime borrowDate = OffsetDateTime.now();
        if (loanDays <= 0) throw new IllegalArgumentException("Loan days must be greater than 0");
        OffsetDateTime dueDate = borrowDate.plusDays(loanDays);

        Loan loan =  new Loan(DomainId.generate(), userId, bookId,  borrowDate, dueDate, LoanStatus.BORROWED);
        loan.registerEvent(new LoanCreatedEvent(
                loan.id().value(),
                userId,
                bookId,
                borrowDate,
                dueDate
        ));

        return loan;
    }

    public static Loan rehydrate(DomainId<Loan> id,
                                 UUID userId,
                                 UUID bookId,
                                 OffsetDateTime borrowDate,
                                 OffsetDateTime dueDate,
                                 OffsetDateTime returnDate,
                                 LoanStatus status) {

        Loan loan = new Loan(id, userId, bookId, borrowDate, dueDate, status);
        if (status == LoanStatus.RETURNED && returnDate != null)
            loan.returnDate = returnDate;
        return loan;
    }

    public Optional<Fine> calculateLateFine() {
        OffsetDateTime endDate = returnDate != null ? returnDate : OffsetDateTime.now();
        if(endDate.isAfter(dueDate)) {
            int daysLate = (int) ChronoUnit.DAYS.between(dueDate, endDate);
            return Optional.of(Fine.forLate(this.userId(), this.id().value(), daysLate, DAILY_LATE_FEE));
        }
        return Optional.empty(); // gecikme yok
    }

    private boolean isActive() {
        return status == LoanStatus.BORROWED || status == LoanStatus.LATE;
    }

    public void markAsReturned() {
        if(!isActive()) throw new IllegalStateException("Only borrowed or late loans can be returned");
        this.status = LoanStatus.RETURNED;
        this.returnDate = OffsetDateTime.now();

        this.registerEvent(new LoanReturnedEvent(
                this.id().value(),
                this.userId,
                this.bookId,
                this.returnDate
        ));
    }

    // Ödünç alınan kitapların teslim tarihi kontrol edilir, gecikmiş ise LATE atanır
    public boolean checkAndMarkOverdue(OffsetDateTime today) {
        if (status == LoanStatus.BORROWED && today.isAfter(dueDate)) {
            status = LoanStatus.LATE;
            return true;
        }
        return false; // Gecikme yoksa false
    }

    //Getters
    public DomainId<Loan> id() {
        return id;
    }
    public UUID userId() {
        return userId;
    }
    public UUID bookId() {
        return bookId;
    }
    public OffsetDateTime borrowDate() {
        return borrowDate;
    }
    public OffsetDateTime dueDate() {
        return dueDate;
    }
    public OffsetDateTime returnDate() {
        return returnDate;
    }
    public LoanStatus status() {
        return status;
    }
}
