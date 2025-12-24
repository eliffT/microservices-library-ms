package com.turkcell.user_service.infrastructure.persistence.entity;
import com.turkcell.common.events.FineCreatedEvent;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "user_fines_projection")
public class UserFine {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id; // Bu, aslında Fine ID'si veya yeni bir ID olabilir.

    @Column(name = "user_id", columnDefinition = "uuid", nullable = false)
    private UUID userId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount; // Ceza miktarı

    @Column(name = "is_paid", nullable = false)
    private boolean isPaid; // Ödenip ödenmediği

    public UserFine() {
    }
    /**
     * Sadece FineCreatedEvent objesini alarak UserFine Entity'sini oluşturan constructor.
     * Bu constructor, event'ten gelen verileri doğrudan entity alanlarına eşler.
     */
    public UserFine(FineCreatedEvent event) {
        // Event'in benzersiz ID'sini (fineId), bu projeksiyon tablosunun PK'sı olarak kullanmak
        // tekrarlı mesajları (idempotency) engellemek için en iyi yoldur.
        this.id = event.fineId();
        this.userId = event.userId();
        this.amount = event.amount();
        this.isPaid = false; // Başlangıçta ödenmemiş (hardcoded değer)

    }

    public UUID id() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public UUID userId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public BigDecimal amount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isPaid() {
        return isPaid;
    }
    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}