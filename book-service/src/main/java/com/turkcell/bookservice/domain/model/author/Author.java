package com.turkcell.bookservice.domain.model.author;

import com.turkcell.bookservice.domain.model.DomainId;

public class Author {
    private final DomainId<Author> id;
    private String fullName;

    private Author(DomainId<Author> id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public static Author create(String fullName) {
        validateFullName(fullName);
        return new Author(DomainId.generate(), fullName);
    }

    public static Author rehydrate(DomainId<Author> id, String fullName) {
        return new Author(id, fullName);
    }

    public void rename(String fullName) {
        validateFullName(fullName);
        this.fullName = fullName;
    }

    private static void validateFullName(String fullName){
        if(fullName == null || fullName.isEmpty())
            throw new IllegalArgumentException("Full name cannot be empty");
        if(fullName.length() > 255)
            throw new IllegalArgumentException("Full name cannot be longer than 255 characters");
    }


    public DomainId<Author> id() {
        return id;
    }
    public String fullName() {
        return fullName;
    }


}
