package com.picus.core.reservation.adapter.out.persistence.repository;

import com.picus.core.reservation.adapter.out.persistence.entity.ReservationEntity;
import com.picus.core.reservation.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, String> {

    @Query("""
        select r
        from ReservationEntity r
        where r.userNo = :userNo
          and (:status is null or r.reservationStatus = :status)
          and (:start is null or r.startTime >= :start)
          and (:end   is null or r.startTime <= :end)
        order by r.startTime desc
    """)
    List<ReservationEntity> findByUserNoAndStatusAndPeriod(
            String userNo,
            LocalDateTime start,
            LocalDateTime end,
            ReservationStatus status
    );
}
