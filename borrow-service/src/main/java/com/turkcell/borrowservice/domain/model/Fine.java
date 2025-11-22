package com.turkcell.borrowservice.domain.model;

import com.turkcell.common.events.FineCreatedEvent;
import com.turkcell.common.events.FinePaidEvent;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public final class Fine extends BaseAggregateRoot {
    private final DomainId<Fine> id;
    private final UUID userId;
    private final UUID loanId;
    private final BigDecimal amount;
    private final String reason;
    private OffsetDateTime createdAt;
    private boolean isPaid = false;
    private OffsetDateTime paymentDate;

    private Fine(DomainId<Fine> id, UUID userId, UUID loanId, BigDecimal amount, String reason) {
        this.id = id;
        this.userId = userId;
        this.loanId = loanId;
        validateAmount(amount);
        validateReason(reason);
        this.amount = amount;
        this.reason = reason;
        this.createdAt = OffsetDateTime.now();
    }

    public static Fine rehydrate(
            DomainId<Fine> id, UUID userId, UUID loanId, BigDecimal amount,
            String reason, OffsetDateTime createdAt, boolean isPaid, OffsetDateTime paymentDate)
    {
        Fine fine = new Fine(id, userId, loanId, amount, reason);
        fine.createdAt = createdAt;
        fine.isPaid = isPaid;
        fine.paymentDate = paymentDate;
        return fine;
    }

    public static Fine of(UUID userId, UUID loanId, BigDecimal amount, String reason) {
        return new Fine(DomainId.generate(), userId, loanId, amount, reason);
    }

    public static Fine forLate(UUID userId, UUID loanId, int daysLate, BigDecimal dailyFee) {
        if(dailyFee == null) throw new IllegalArgumentException("Daily fee cannot be null");
        if(daysLate <= 0) throw new IllegalArgumentException("Days late must be > 0");
        Fine fine =  new Fine(DomainId.generate(),userId, loanId,
                dailyFee.multiply(BigDecimal.valueOf(daysLate)),
                "Late return");

        fine.registerEvent(new FineCreatedEvent(
                fine.id().value(), fine.userId(), fine.loanId(), fine.amount()
        ));
        return fine;
    }

    public static Fine forDamage(UUID userId, UUID loanId,BigDecimal bookPrice) {
        return new Fine(DomainId.generate(), userId, loanId, bookPrice, "Damaged book");
    }

    public void markAsPaid() {
        if(this.isPaid) throw new IllegalStateException("Fine is already paid.");

        this.isPaid = true;
        this.paymentDate = OffsetDateTime.now();

        this.registerEvent(new FinePaidEvent(
                this.id().value(), this.userId(), this.amount(), this.paymentDate
        ));
    }

    private static void validateAmount(BigDecimal amount){
        if(amount == null)
            throw new IllegalArgumentException("Amount cannot be null");
        if(amount.signum() < 0)
            throw new IllegalArgumentException("Amount must be positive");
    }

    private static void validateReason(String reason){
        if(reason == null || reason.isBlank()) throw new IllegalArgumentException("Reason cannot be null or empty");
    }

    // Getters
    public DomainId<Fine> id() {
        return id;
    }
    public UUID userId() {
        return userId;
    }
    public UUID loanId() {
        return loanId;
    }
    public BigDecimal amount() {
        return amount;
    }
    public String reason() {
        return reason;
    }
    public OffsetDateTime createdAt() {
        return createdAt;
    }
    public boolean isPaid() {
        return isPaid;
    }
    public OffsetDateTime paymentDate() {
        return paymentDate;
    }
}

