package com.turkcell.bookservice.domain.model.book;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Benzersizlik garantisi için UUID tabanlı değer üreten metot. -> generate()
 * Bu metot veritabanı kontrolüne gerek kalmadan yüksek olasılıkla benzersiz bir değer üretir.
 */
public record Isbn(String value) implements Serializable {
    public Isbn {
        Objects.requireNonNull(value, "value is null");
        if(value.isBlank()) throw new IllegalArgumentException("ISBN cannot be blank");
    }

    public static Isbn generate() {
        // UUID kullanılarak pratik olarak benzersiz (collision risk'i ihmal edilebilir) bir değer üretilir.
        return new Isbn("AUTO-ISBN-" + UUID.randomUUID().toString());
    }
}
