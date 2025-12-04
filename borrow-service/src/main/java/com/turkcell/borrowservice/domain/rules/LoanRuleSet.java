package com.turkcell.borrowservice.domain.rules;

public enum LoanRuleSet {
    STANDARD(3, 14),
    GOLD(5, 21),
    BANNED(0, 0); // BANNED için limit 0'dır, Handler'da kontrol edilir.

    private final int maxLoans;
    private final int loanDays;

    LoanRuleSet(int maxLoans, int loanDays) {
        this.maxLoans = maxLoans;
        this.loanDays = loanDays;
    }

    public int maxLoans() {
        return maxLoans;
    }
    public int loanDays() {
        return loanDays;
    }

    public static LoanRuleSet fromString(String level) {
        try {
            return LoanRuleSet.valueOf(level.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BANNED;
        }
    }
}
