package com.turkcell.borrowservice.application.queries;

import java.util.UUID;

// Belirli bir kullanıcının cezalarını ödeme durumuna göre filtreleyerek listeleme sorgusu.

public record ListUserFinesQuery (
        UUID userId,
        Boolean isPaidFilter // true: Ödenmiş, false: Ödenmemiş (null: Hepsi)
) {}
