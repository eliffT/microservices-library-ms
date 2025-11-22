package com.turkcell.bookservice.domain.model.book;

import com.turkcell.bookservice.domain.model.BaseAggregateRoot;
import com.turkcell.bookservice.domain.model.DomainId;
import com.turkcell.bookservice.domain.model.author.Author;
import com.turkcell.bookservice.domain.model.category.Category;
import com.turkcell.bookservice.domain.model.publisher.Publisher;
import com.turkcell.common.events.BookCreatedEvent;
import com.turkcell.common.events.BookStockChangedEvent;

import java.math.BigDecimal;

public class Book extends BaseAggregateRoot {
    private final DomainId<Book> id;
    private final DomainId<Author> authorId;
    private final DomainId<Publisher> publisherId;
    private final DomainId<Category> categoryId;

    private final Isbn isbn;
    private String title;
    private Integer year;
    private String language;
    private Integer totalCopies;
    private Integer availableCopies;
    private BookStatus status;
    private BigDecimal price;



    private Book(DomainId<Book> id, Isbn isbn, String title, Integer year, String language, Integer totalCopies,
                 Integer availableCopies, BookStatus status, DomainId<Author> authorId,
                 DomainId<Publisher> publisherId, DomainId<Category> categoryId, BigDecimal price) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.year = year;
        this.language = language;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.status = status;
        this.authorId = authorId;
        this.publisherId = publisherId;
        this.categoryId = categoryId;
        this.price = price;
    }


    public static Book create(String title, Integer year, String language, Integer totalCopies, DomainId<Author> authorId,
                              DomainId<Publisher> publisherId, DomainId<Category> categoryId, BigDecimal price) {
        validateTitle(title);
        checkYear(year);
        validateLanguage(language);
        checkTotalCopiesAmount(totalCopies);
        checkPrice(price);
        Isbn isbn = Isbn.generate();

        Book book =  new Book(DomainId.generate(), isbn,  title, year, language,
                totalCopies, totalCopies, BookStatus.ACTIVE,
                authorId, publisherId, categoryId, price);

        book.registerEvent(new BookCreatedEvent(
                book.id().value(), book.isbn.value(), book.title,
                authorId.value(), publisherId.value(), categoryId.value(), book.totalCopies));

        return book;
    }

    public static Book rehydrate(DomainId<Book> id, Isbn isbn, String title, Integer year, String language,
                                 Integer totalCopies, Integer availableCopies,  BookStatus status, DomainId<Author> authorId,
                                 DomainId<Publisher> publisherId, DomainId<Category> categoryId,  BigDecimal price) {

        Book b = new Book(id, isbn, title, year, language, totalCopies, availableCopies,  status, authorId,
                publisherId, categoryId, price);
        return b;
    }

    public void increaseStock(Integer quantity) {
        if (quantity == null || quantity <= 0)
            throw new IllegalArgumentException("Quantity must be greater than 0");

        this.availableCopies += quantity;
        ensureStockConsistency();

        this.registerEvent(new BookStockChangedEvent(
                this.id().value(),
                this.availableCopies,
                "StockIncreased"
        ));
    }

    public void decreaseStock(Integer quantity) {
        if (quantity == null || quantity <= 0)
            throw new IllegalArgumentException("Quantity must be > 0");
        if (this.availableCopies < quantity)
            throw new IllegalArgumentException("Not enough copies");

        this.availableCopies -= quantity;
        ensureStockConsistency();

        this.registerEvent(new BookStockChangedEvent(
                this.id().value(),
                this.availableCopies,
                "StockDecreased"
        ));
    }

    public void changeTotalStock(Integer newTotalCopies) {
        checkTotalCopiesAmount(newTotalCopies); // amount < 0 olmasın

        Integer delta = newTotalCopies - this.totalCopies;

        if (delta > 0) {
            // Stok artırılıyor
            this.totalCopies = newTotalCopies;
            this.availableCopies += delta; // Yeni gelen stok da kullanılabilir hale gelir
        } else if (delta < 0) {
            // Stok azaltılıyor
            if (newTotalCopies < this.availableCopies) {
                throw new IllegalArgumentException("Cannot reduce total stock below currently available copies");
            }
            this.totalCopies = newTotalCopies;
            // availableCopies değişmez
        }
        // Eğer delta = 0 ise, değişiklik yok.

        ensureStockConsistency();
        // StockChangedEvent yayınlanmalı, çünkü toplam stok değişimi availableCopies'i etkilemiş olabilir.
        this.registerEvent(new BookStockChangedEvent(
                this.id().value(),
                this.availableCopies,
                "TotalStockChanged"
        ));
    }

    public void rename(String title) {
        validateTitle(title);
        this.title = title;
    }
    public void changeYear(Integer year) {
        checkYear(year);
        this.year = year;
    }
    public void changeLanguage(String language) {
        validateLanguage(language);
        this.language = language;
    }
    public void activate(){
        status = BookStatus.ACTIVE;
    }
    public void deactivate(){
        status = BookStatus.INACTIVE;
    }
    public void resetAvailableCopies() {
        this.availableCopies = this.totalCopies;
        this.status = this.availableCopies > 0 ? BookStatus.ACTIVE : BookStatus.INACTIVE;
    }

    // Validation
    private static void checkYear(Integer year) {
        if (year == null) {
            throw new IllegalArgumentException("Year cannot be null");
        }
        int currentYear = java.time.Year.now().getValue();

        if (year < 1500) {
            throw new IllegalArgumentException("Year must be greater than 1500");
        }

        if (year > currentYear) {
            throw new IllegalArgumentException("Year cannot be in the future");
        }
    }
    private static void checkPrice(BigDecimal price) {
        if (price == null) throw new IllegalArgumentException("Price cannot be null");
        if (price.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Price cannot be negative");
    }
    private static void validateTitle(String title){
        if(title == null || title.trim().isEmpty())
            throw new IllegalArgumentException("Title cannot be null or whitespace");
        if(title.length() >= 255)
            throw new IllegalArgumentException("Title length must be less than 255 characters");
    }
    private static void validateLanguage(String language){
        if(language == null || language.trim().isEmpty())
            throw new IllegalArgumentException("Language cannot be null or empty");
        if(language.length() < 2 || language.length() > 15)
            throw new IllegalArgumentException("Language length must be between 2 and 15 characters");
    }
    private static void checkTotalCopiesAmount(Integer amount){
        if (amount == null || amount < 0)
            throw new IllegalArgumentException("Total copies cannot be negative");
    }
    private void ensureStockConsistency() {
        if (this.availableCopies < 0)
            throw new IllegalStateException("Available copies cannot be negative");
        if (availableCopies > totalCopies) {
            throw new IllegalArgumentException("Available copies cannot exceed total copies");
        }
    }


    // Getters
    public Isbn isbn() {
        return isbn;}
    public DomainId<Book> id() {
        return id;
    }
    public String title() {
        return title;
    }
    public Integer year() {
        return year;
    }
    public String language() {
        return language;
    }
    public Integer totalCopies() {
        return totalCopies;
    }
    public Integer availableCopies() {
        return availableCopies;
    }
    public BookStatus status() {
        return status;
    }
    public DomainId<Author> authorId() { return authorId; }
    public DomainId<Publisher> publisherId() { return publisherId; }
    public DomainId<Category> categoryId() { return categoryId; }
    public BigDecimal price() {
        return price;
    }
}
