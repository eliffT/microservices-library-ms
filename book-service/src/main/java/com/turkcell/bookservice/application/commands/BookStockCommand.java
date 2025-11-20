package com.turkcell.bookservice.application.commands;

import java.util.UUID;


// Birden fazla nedenle (Rezervasyon Oluşturma, Kitap Ödünç Verme vb.) kitabın
// stoğunu düşürmek için kullanılan genel komut.

public record BookStockCommand(
        UUID bookId,
        UUID eventId
) {
}