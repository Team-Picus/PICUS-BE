package com.picus.core.reservation.adapter.out.persistence.entity;

import com.picus.core.expert.domain.vo.PriceThemeType;
import com.picus.core.reservation.adapter.out.persistence.entity.vo.OptionSnapshot;
import com.picus.core.reservation.adapter.out.persistence.entity.vo.PackageSnapshot;
import com.picus.core.reservation.domain.ReservationStatus;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@Table(name = "reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReservationEntity {

    @Id @Tsid
    private String reservationNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus reservationStatus;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private String requestDetail;

    @Column(nullable = false)
    private PriceThemeType themeType;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private String userNo;

    @Column(nullable = false)
    private String expertNo;

    @ElementCollection
    @CollectionTable(name = "optionSnapshots", joinColumns = @JoinColumn(name = "reservation_no"))
    private List<OptionSnapshot> optionSnapshots = new ArrayList<>();

    @Embedded
    private PackageSnapshot packageSnapshot;

    @PrePersist
    protected void init() {
        this.reservationStatus = ReservationStatus.REQUESTED;
    }
}
