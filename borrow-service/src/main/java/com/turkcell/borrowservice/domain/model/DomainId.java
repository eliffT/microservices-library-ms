package com.turkcell.borrowservice.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public record DomainId<T>(UUID value) implements Serializable {
    public DomainId {
        Objects.requireNonNull(value, "value is null");
    }
    @JsonCreator
    public static <T> DomainId<T> fromString(String value) {
        return new DomainId<>(UUID.fromString(value));
    }

    @JsonValue
    public String asString() {
        return value.toString();
    }

    public static <T> DomainId<T> from(UUID id) {
        return new DomainId<>(id);
    }


    public static <T> DomainId<T> generate() {
        return new DomainId<>(UUID.randomUUID());
    }

}


