package com.turkcell.borrowservice.application.mapper;

import com.turkcell.borrowservice.application.queries.dtos.ReservationDetailsDto;
import com.turkcell.borrowservice.domain.model.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationAppMapper {

    public ReservationDetailsDto toDto(Reservation reservation) {
        return new ReservationDetailsDto(
                reservation.id().value(),
                reservation.bookId(),
                reservation.reservationDate(),
                reservation.expireDate(),
                reservation.status()
        );
    }

}
