package com.turkcell.user_service.infrastructure.persistence.entity;

public enum MembershipLevel {
    STANDARD(3, 14),
    GOLD(5, 21),
    BANNED(0, 0);

    private final int maxLoans;
    private final int loanDays;

    MembershipLevel(int maxLoans, int loanDays) {
        this.maxLoans = maxLoans;
        this.loanDays = loanDays;
    }

    public int getMaxLoans() {
        return maxLoans;
    }

    public int getLoanDays() {
        return loanDays;
    }

}
