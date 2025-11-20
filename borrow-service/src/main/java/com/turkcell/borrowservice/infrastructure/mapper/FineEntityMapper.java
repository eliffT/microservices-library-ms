package com.turkcell.borrowservice.infrastructure.mapper;

import com.turkcell.borrowservice.domain.model.DomainId;
import com.turkcell.borrowservice.domain.model.Fine;
import com.turkcell.borrowservice.infrastructure.persistence.entity.FineEntity;
import org.springframework.stereotype.Component;

@Component
public class FineEntityMapper {

    // Domain -> Entity
    public FineEntity toEntity(Fine fine) {
        if (fine == null)  return null;

        FineEntity entity = new FineEntity();
        entity.setId(fine.id().value()); // VO olduğu için entity ID burada üretiliyor
        entity.setAmount(fine.amount());
        entity.setReason(fine.reason());

        entity.setUserId(fine.userId());
        entity.setLoanId(fine.loanId());

        entity.setCreatedAt(fine.createdAt());
        entity.setPaid(fine.isPaid());
        entity.setPaymentDate(fine.paymentDate());
        return entity;
    }

    // Entity -> Domain
    public Fine toDomain(FineEntity entity) {
        return Fine.rehydrate(
                new DomainId<>(entity.getId()),
                entity.getUserId(),
                entity.getLoanId(),
                entity.getAmount(),
                entity.getReason(),
                entity.getCreatedAt(),
                entity.isPaid(),
                entity.getPaymentDate());
    }
}
