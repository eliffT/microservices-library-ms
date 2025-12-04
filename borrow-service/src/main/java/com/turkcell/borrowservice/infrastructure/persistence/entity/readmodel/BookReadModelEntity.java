package com.turkcell.borrowservice.infrastructure.persistence.entity.readmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "book_read_models")
public class BookReadModelEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID bookId;

    private int availableStock;

    @Column(nullable = false)
    private boolean isAvailable; // Ödünç verilebilir durumda mı?

    private String title;

    public BookReadModelEntity() {}
    public BookReadModelEntity(UUID bookId, boolean isAvailable) {
        this.bookId = bookId;
        this.isAvailable = isAvailable;
    }

    // Getters ve Setters
    public UUID bookId() {
        return bookId;
    }
    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public int availableStock() {
        return availableStock;
    }
    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String title() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}

