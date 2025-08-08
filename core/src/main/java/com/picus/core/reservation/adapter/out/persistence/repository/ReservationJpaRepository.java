package com.picus.core.reservation.adapter.out.persistence.repository;

import com.picus.core.reservation.adapter.out.persistence.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, String> {
}
