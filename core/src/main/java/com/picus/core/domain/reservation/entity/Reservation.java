package com.picus.core.domain.reservation.entity;

import com.picus.core.domain.post.entity.OptionTable;
import com.picus.core.global.common.BaseEntity;
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

//    private Price price;

    private String detail;

    private ReservationStatus status;

    @Column(nullable = false)
    private Long expertAccountNo;

    public Reservation(Schedule schedule, OptionTable price, String detail, Long expertAccountNo) {
        this.schedule = schedule;
//        this.price = price;
        this.detail = detail;
        this.status = ReservationStatus.CHECKING;
        this.expertAccountNo = expertAccountNo;
    }
}
