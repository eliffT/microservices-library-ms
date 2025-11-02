package com.turkcell.bookservice.domain.model.book;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public record Isbn(String value) implements Serializable {
    public Isbn {
        Objects.requireNonNull(value, "value is null");
        if(value.isBlank()) throw new IllegalArgumentException("ISBN cannot be blank");
    }

    public static Isbn generate() {
        return new Isbn("ISBN-" + UUID.randomUUID().toString().substring(0, 13));
    }
}
