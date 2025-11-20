package com.turkcell.borrowservice.infrastructure.mapper;


import com.turkcell.borrowservice.domain.model.DomainId;
import com.turkcell.borrowservice.domain.model.Reservation;
import com.turkcell.borrowservice.infrastructure.persistence.entity.ReservationEntity;
import org.springframework.stereotype.Component;


@Component
public class ReservationEntityMapper {

    public ReservationEntity toEntity(Reservation r) {
        if (r == null) return null;

        ReservationEntity entity = new ReservationEntity();
        entity.setId(r.id().value());
        entity.setUserId(r.userId());
        entity.setBookId(r.bookId());
        entity.setReservationDate(r.reservationDate());
        entity.setExpireDate(r.expireDate());
        entity.setStatus(r.status());


        return entity;
    }

    public Reservation toDomain(ReservationEntity entity) {
        if (entity == null) return null;


        return Reservation.rehydrate(
                new DomainId<Reservation>(entity.getId()),
                entity.getUserId(),
                entity.getBookId(),
                entity.getReservationDate(),
                entity.getExpireDate(),
                entity.getStatus()
        );
    }
}
