package com.turkcell.bookservice.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "publishers")
public class PublisherEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "name",  nullable = false, length = 255)
    private String name;

    @OneToMany(mappedBy = "publisher")
    private List<BookEntity> books;

    public PublisherEntity() {} // JPA için gerekli

    // Mapper için ID constructor
    public PublisherEntity(UUID id) {
        this.id = id;
    }
    public UUID id() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BookEntity> books() {
        return books;
    }

    public void setBooks(List<BookEntity> books) {
        this.books = books;
    }
}