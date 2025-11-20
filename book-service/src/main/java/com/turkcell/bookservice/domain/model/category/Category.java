package com.turkcell.bookservice.domain.model.category;

import com.turkcell.bookservice.domain.model.BaseAggregateRoot;
import com.turkcell.bookservice.domain.model.DomainId;

public class Category extends BaseAggregateRoot {

    private final DomainId<Category> id;
    private String name;
    private String description;

    private Category(DomainId<Category> id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static Category create(String name, String description) {
        validateName(name);
        validateDescription(description);
        return new Category(DomainId.generate(),  name, description );
    }

    public static Category rehydrate(DomainId<Category> id, String name, String description)
    {
        return new Category(id, name, description);
    }

    // Domain Setters
    public void rename(String name) {
        validateName(name);
        this.name = name;
    }
    public void changeDescription(String description) {
        validateDescription(description);
        this.description = description;
    }

    // Validation
    private static void  validateName(String name) {
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty");
        if(name.length() > 255)
            throw new IllegalArgumentException("Name length must be less than 255 characters");
    }
    private static void validateDescription(String description) {
        if(description == null || description.isEmpty())
            throw new IllegalArgumentException("Description cannot be null or empty");
        if(description.length() > 255)
            throw new IllegalArgumentException("Description length must be less than 255 characters");
    }

    // Getters
    public DomainId<Category> id() {
        return id;
    }
    public String name() {
        return name;
    }
    public String description() {
        return description;
    }

}
