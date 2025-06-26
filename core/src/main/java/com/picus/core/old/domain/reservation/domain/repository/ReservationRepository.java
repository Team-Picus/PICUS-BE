package com.picus.core.old.domain.reservation.domain.repository;


import com.picus.core.old.domain.reservation.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
