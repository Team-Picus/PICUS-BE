package com.picus.core.reservation.adapter.out.persistence;

import com.picus.core.reservation.adapter.out.persistence.entity.ReservationEntity;
import com.picus.core.reservation.adapter.out.persistence.mapper.ReservationPersistenceMapper;
import com.picus.core.reservation.adapter.out.persistence.repository.ReservationJpaRepository;
import com.picus.core.reservation.application.port.out.ReservationCreatePort;
import com.picus.core.reservation.domain.Reservation;
import com.picus.core.shared.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class ReservationPersistenceAdapter implements ReservationCreatePort {

    private final ReservationPersistenceMapper reservationPersistenceMapper;
    private final ReservationJpaRepository reservationJpaRepository;

    @Override
    public void create(Reservation reservation) {
        ReservationEntity entity = reservationPersistenceMapper.toEntity(reservation);
        reservationJpaRepository.save(entity);
    }
}
