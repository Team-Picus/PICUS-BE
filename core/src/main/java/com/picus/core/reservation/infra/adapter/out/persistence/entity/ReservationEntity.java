package com.picus.core.reservation.infra.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@Table(name = "reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReservationEntity {

    @Id @Tsid
    private String reservationNo;

    @NotNull(message = "reservationStatus 값은 필수입니다.")
    private ReservationStatus reservationStatus;

    @NotBlank(message = "place 값은 필수입니다.")
    private String place;

    @NotNull(message = "startTime 값은 필수입니다.")
    private LocalDateTime startTime;

    private String request;

    // 주문 도메인


    @PrePersist
    protected void init() {
        this.reservationStatus = ReservationStatus.PENDING;
    }
}
