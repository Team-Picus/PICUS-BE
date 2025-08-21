package com.picus.core.reservation.adapter.out.persistence;

import com.picus.core.reservation.adapter.out.persistence.entity.ReservationEntity;
import com.picus.core.reservation.adapter.out.persistence.mapper.ReservationPersistenceMapper;
import com.picus.core.reservation.adapter.out.persistence.repository.ReservationJpaRepository;
import com.picus.core.reservation.application.port.out.ReservationCreatePort;
import com.picus.core.reservation.application.port.out.ReservationDeletePort;
import com.picus.core.reservation.application.port.out.ReservationReadPort;
import com.picus.core.reservation.application.port.out.ReservationUpdatePort;
import com.picus.core.reservation.domain.Reservation;
import com.picus.core.reservation.domain.ReservationStatus;
import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.shared.exception.RestApiException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.RESERVATION_NOT_FOUND;

@PersistenceAdapter
@RequiredArgsConstructor
public class ReservationPersistenceAdapter
        implements ReservationCreatePort, ReservationReadPort, ReservationUpdatePort, ReservationDeletePort {

    private final ReservationPersistenceMapper reservationPersistenceMapper;
    private final ReservationJpaRepository reservationJpaRepository;

    @Override
    public void create(Reservation reservation) {
        ReservationEntity entity = reservationPersistenceMapper.toEntity(reservation);
        reservationJpaRepository.save(entity);
    }

    @Override
    public Reservation findById(String reservationNo) {
        return reservationJpaRepository.findById(reservationNo)
                .map(reservationPersistenceMapper::toDomain)
                .orElseThrow(() -> new RestApiException(RESERVATION_NOT_FOUND));
    }

    @Override
    public List<Reservation> findByUserNoWithFilter(String userNo, LocalDateTime start, ReservationStatus status) {
        return reservationJpaRepository.findByUserNoAndStatusAndPeriod(userNo, start, LocalDateTime.now(), status).stream()
                .map(reservationPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void update(String reservationNo, ReservationStatus status) {
        Reservation reservation = findById(reservationNo);
        reservation.updateStatus(status);
    }

    @Override
    public void delete(String reservationNo) {
        reservationJpaRepository.deleteById(reservationNo);
    }
}
