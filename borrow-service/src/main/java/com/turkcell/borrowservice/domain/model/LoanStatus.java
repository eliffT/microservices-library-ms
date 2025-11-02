package com.turkcell.borrowservice.domain.model;

public enum LoanStatus {
    BORROWED,   // Kitap ödünç alınmış
    RETURNED,   // Kitap iade edilmiş
    LATE,        // Teslim tarihi geçmiş
    CANCELLED

}
