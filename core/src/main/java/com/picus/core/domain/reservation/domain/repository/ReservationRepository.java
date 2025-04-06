package com.picus.core.domain.reservation.domain.repository;


import com.picus.core.domain.reservation.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
