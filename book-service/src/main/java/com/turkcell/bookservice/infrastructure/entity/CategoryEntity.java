package com.turkcell.bookservice.infrastructure.entity;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categories")
public class CategoryEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "category_name", nullable = false, unique = true, length = 50)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<BookEntity> Books;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

    public CategoryEntity() {} // JPA için gerekli

    // Mapper için ID constructor
    public CategoryEntity(UUID id) {
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

    public List<BookEntity> Books() {
        return Books;
    }

    public void setBooks(List<BookEntity> books) {
        Books = books;
    }

    public String description() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}