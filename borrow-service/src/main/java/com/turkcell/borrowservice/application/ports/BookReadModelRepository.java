package com.turkcell.borrowservice.application.ports;

import com.turkcell.borrowservice.application.ports.output.BookReadModel;
import java.util.Optional;
import java.util.UUID;

public interface BookReadModelRepository {

    Optional<BookReadModel> findById(UUID bookId);

    BookReadModel save(BookReadModel model);

    // Rezervasyon kuralları için kritik olan anlık stok bilgisini döndürür.
    int getAvailableStock(UUID bookId);

    // Kitabın ödünç alınıp alınamayacağını hızlıca kontrol etmek için kullanılır.
    boolean isBookAvailable(UUID bookId);

    void deleteById(UUID bookId);




}
