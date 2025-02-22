package com.picus.core.domain.reservation.entity;

import com.picus.core.global.common.base.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id @Tsid
    @Column(name = "reservation_no")
    private Long id;

    private Schedule schedule;

    private String detail;

    private ReservationStatus status;

    @Column(nullable = false)
    private Long expertAccountNo;

    public Reservation(Schedule schedule, String detail, Long expertAccountNo) {
        this.schedule = schedule;
        this.detail = detail;
        this.status = ReservationStatus.CHECKING;
        this.expertAccountNo = expertAccountNo;
    }
}

// TODO List<Option, OptionDiscount, Discount> 를 value로 캡처하기
// 1. post id
// 2. List<option>  -> value
// 3. discountPolicy -> value