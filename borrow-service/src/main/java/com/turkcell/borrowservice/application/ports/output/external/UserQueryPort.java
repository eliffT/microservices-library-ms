package com.turkcell.borrowservice.application.ports.output.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

// User Service'e senkron sorgu atmak için Port
// 1. @FeignClient ile Feign'ı etkinleştiriyoruz.
//    name = "user-service": Eureka'da kayıtlı olan User Service adı.
//    path = "/api/users": User Service'teki ana Controller yolunu belirtir
@FeignClient(name = "user-service", path = "/api/v1//users")
public interface UserQueryPort {
    /**
     * Verilen kullanıcının anlık üyelik seviyesini (STANDARD, GOLD, BANNED) getirir.
     * Bu, Loan ve Reservation işlemlerinden hemen önce kritik iş kuralı doğrulaması
     * (örn. BANNED kontrolü) için kullanılır.
     **/

    // 2. Metot imzası: Port'un tanımını kullanır.
    //    Feign, bu metot çağrıldığında otomatik olarak HTTP GET isteği gönderir.
    @GetMapping("/{id}/status")
    String getMembershipLevel(@PathVariable("id") UUID id);

}