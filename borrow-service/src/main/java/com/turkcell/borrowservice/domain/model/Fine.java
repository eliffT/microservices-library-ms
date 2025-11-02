package com.turkcell.borrowservice.domain.model;


import java.math.BigDecimal;
import java.time.LocalDate;

public final class Fine {
    private final BigDecimal amount;
    private final String reason;
    private final LocalDate createdAt;

    private Fine(BigDecimal amount, String reason) {
        validateAmount(amount);
        validateReason(reason);
        this.amount = amount;
        this.reason = reason;
        this.createdAt = LocalDate.now();
    }

    public static Fine of(BigDecimal amount, String reason) {
        return new Fine(amount, reason);
    }

    public static Fine forLate(int daysLate, BigDecimal dailyFee) {
        if(dailyFee == null) throw new IllegalArgumentException("Daily fee cannot be null");
        if(daysLate <= 0) throw new IllegalArgumentException("Days late must be > 0");
        return new Fine(dailyFee.multiply(BigDecimal.valueOf(daysLate)), "Late return");
    }

    public static Fine forDamage(BigDecimal bookPrice) {
        return new Fine(bookPrice, "Damaged book");
    }

    private static void validateAmount(BigDecimal amount){
        if(amount == null)
            throw new IllegalArgumentException("Amount cannot be null");
        if(amount.signum() < 0)
            throw new IllegalArgumentException("Amount must be positive");
    }

    private static void validateReason(String reason){
        if(reason == null || reason.isEmpty()) throw new IllegalArgumentException("Reason cannot be null or empty");
    }


    // Getters
    public BigDecimal amount() { return amount; }
    public String reason() { return reason; }
    public LocalDate createdAt() { return createdAt; }
}

