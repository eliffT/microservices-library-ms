package com.turkcell.borrowservice.application.ports.output;

import java.util.UUID;

public record BookReadModel(UUID bookId, boolean isAvailable, int availableStock, String title) {
}

