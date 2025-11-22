package com.turkcell.borrowservice.infrastructure.persistence.entity;

import com.turkcell.borrowservice.domain.model.enumstatus.LoanStatus;
import jakarta.persistence.*;


import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "loans")
public class LoanEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name= "user_uuid", columnDefinition = "uuid")
    private UUID userId;

    @Column(name = "book_uuid" , columnDefinition = "uuid")
    private UUID bookId;

    @Column(name = "borrow_date", nullable = false)
    private OffsetDateTime borrowDate;

    @Column(name = "due_date", nullable = false)
    private OffsetDateTime dueDate;

    @Column(name = "return_date")
    private OffsetDateTime returnDate;

    @Enumerated(EnumType.STRING)  // Enum değerlerini String olarak kaydedilir
    private LoanStatus status;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getBookId() {
        return bookId;
    }
    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public OffsetDateTime getBorrowDate() {
        return borrowDate;
    }
    public void setBorrowDate(OffsetDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public OffsetDateTime getDueDate() {
        return dueDate;
    }
    public void setDueDate(OffsetDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public OffsetDateTime getReturnDate() {
        return returnDate;
    }
    public void setReturnDate(OffsetDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public LoanStatus getStatus() {
        return status;
    }
    public void setStatus(LoanStatus status) {
        this.status = status;
    }
}
