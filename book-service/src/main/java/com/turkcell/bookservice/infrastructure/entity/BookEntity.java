package com.turkcell.bookservice.infrastructure.entity;

import com.turkcell.bookservice.domain.model.book.BookStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "year",  nullable = false, length = 4)
    private Integer year;

    @Column(name = "lang", nullable = false, length = 10)
    private String language;

    @Column(name = "total_copies", nullable = false)
    private int totalCopies;

    @Column(name = "available_copies", nullable = false)
    private int availableCopies;

    @Column(name = "price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @ManyToOne()
    //@JoinColumn(name = "category_id", nullable = false)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne()
    @JoinColumn(name = "author_id")     // FK books tablosunda - owner side
    private AuthorEntity author;

    @ManyToOne()
    @JoinColumn(name = "publisher_id")
    private PublisherEntity publisher;

    public BookEntity() {
    }

    public BookEntity(UUID id) {
        this.id = id;
    }

    public UUID id() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String isbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String title() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Integer year() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }

    public String language() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

    public int totalCopies() {
        return totalCopies;
    }
    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int availableCopies() {
        return availableCopies;
    }
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public BookStatus status() {
        return status;
    }
    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public CategoryEntity category() {
        return category;
    }
    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public AuthorEntity author() {
        return author;
    }
    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }

    public PublisherEntity publisher() {
        return publisher;
    }
    public void setPublisher(PublisherEntity publisher) {
        this.publisher = publisher;
    }

    public BigDecimal price() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
