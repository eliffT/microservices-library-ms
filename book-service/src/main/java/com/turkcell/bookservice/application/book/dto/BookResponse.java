package com.turkcell.bookservice.application.book.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BookResponse(UUID id, UUID authorId, UUID categoryId,
                           String title, Integer year, String language,
                           Integer totalCopies,
                           BigDecimal price) {
}

