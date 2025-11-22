package com.turkcell.borrowservice.domain.model;

import com.turkcell.borrowservice.domain.model.enumstatus.ReservationStatus;
import com.turkcell.common.events.ReservationCancelledEvent;
import com.turkcell.common.events.ReservationCreatedEvent;
import com.turkcell.common.events.ReservationFulfilledEvent;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Reservation extends BaseAggregateRoot{

    private final DomainId<Reservation> id;
    private final UUID userId;
    private final UUID bookId;
    private OffsetDateTime reservationDate;
    private OffsetDateTime expireDate;
    private ReservationStatus status;


    private Reservation(DomainId<Reservation>  id, UUID userId, UUID bookId, OffsetDateTime reservationDate,
                        OffsetDateTime expireDate, ReservationStatus status) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.reservationDate = reservationDate;
        this.expireDate = expireDate;
        this.status = status;
    }

    public static Reservation create(UUID userId, UUID bookId){

        if (userId == null) throw new IllegalArgumentException("User cannot be null");
        if (bookId == null) throw new IllegalArgumentException("Book cannot be null");

        Reservation reservation = new Reservation
                (DomainId.generate(), userId, bookId, OffsetDateTime.now(), null, ReservationStatus.ACTIVE);

        reservation.registerEvent(new ReservationCreatedEvent(
                reservation.id().value(),
                userId,
                bookId
        ));
        return reservation;
    }

    public static Reservation rehydrate(DomainId<Reservation> id, UUID userId, UUID bookId,
                                        OffsetDateTime reservationDate, OffsetDateTime expireDate, ReservationStatus status)
    {
        return new Reservation(id, userId, bookId, reservationDate, expireDate, status);
    }

    // Rezervasyon stok geldikten sonra aktif hale getirildiğinde kullanılır
    public void fulfill(int pickupHours) {
        if (status != ReservationStatus.ACTIVE) {
            throw new IllegalStateException("Only active reservations can be fulfilled.");
        }

        this.status = ReservationStatus.FULFILLED;

        OffsetDateTime newExpireDate = OffsetDateTime.now().plusHours(pickupHours);
        this.expireDate = newExpireDate;

        this.registerEvent(new ReservationFulfilledEvent(
                this.id().value(),
                this.userId,
                this.bookId,
                newExpireDate
        ));
    }

    // Kullanıcı tarafıda iptal
    public void cancel() {
        if (status != ReservationStatus.ACTIVE && status != ReservationStatus.FULFILLED)
            throw new IllegalStateException("Only active or fulfilled reservations can be cancelled");
        status = ReservationStatus.CANCELLED;

        this.registerEvent(new ReservationCancelledEvent(
                this.id().value(),
                this.bookId,
                "Manual"
        ));
    }

    public boolean isExpired(OffsetDateTime today) {
        if (expireDate == null) return false;
        return today.isAfter(expireDate);
    }

    // Rezervasyon süresi doldu
    public void markAsExpired() {
        if (status != ReservationStatus.FULFILLED)
            throw new IllegalStateException("Only fulfilled reservations can be marked as expired.");

        if (expireDate == null)
            throw new IllegalStateException("For reservations that have not expired, expireAt cannot be null.");

        if (!isExpired(OffsetDateTime.now()))
            throw new IllegalStateException("Cannot mark as expired before expiration date");

        // expireAt geçerse rezervasyon CANCELLED olur.
        status = ReservationStatus.CANCELLED;
        this.registerEvent(new ReservationCancelledEvent(
                this.id().value(),
                this.bookId,
                "Expired"
        ));
    }

    public DomainId<Reservation>  id() {
        return id;
    }
    public UUID userId() { return userId; }
    public UUID bookId() { return bookId; }
    public OffsetDateTime reservationDate() {
        return reservationDate;
    }
    public OffsetDateTime expireDate() {
        return expireDate;
    }
    public ReservationStatus status() {
        return status;
    }

}
