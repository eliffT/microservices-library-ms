package com.turkcell.borrowservice.application.ports.output.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "book-service", path = "/api/v1/books")
public interface BookQueryPort {
    /**
     * Verilen kitap ID'si için anlık availableCopies (ödünç verilebilir stok) miktarını getirir.
     * Bu, Loan ve Reservation kararları için kritik doğrulama sağlar.
     **/

    // Book Service'teki endpoint varsayımı: /api/v1/books/{bookId}/stock
    @GetMapping("/{bookId}/stock")
    int getAvailableCopies(@PathVariable("bookId") UUID bookId);
}
