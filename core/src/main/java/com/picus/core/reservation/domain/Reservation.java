package com.picus.core.reservation.domain;

import com.picus.core.expert.domain.vo.PriceThemeType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class Reservation {

    private String reservationNo;
    private ReservationStatus reservationStatus;

    // 스케줄
    private String place;
    private LocalDateTime startTime;

    // 가격 및 디테일
    private PriceThemeType themeType;
    private String requestDetail;
    private SelectedPackage selectedPackage;
    private List<SelectedOption> selectedOptions;
    private Integer totalPrice;

    // FK
    private String userNo;
    private String expertNo;

    public boolean isExpert(String expertNo) {
        return this.expertNo.equals(expertNo);
    }

    public boolean isPending() {
        return this.reservationStatus.equals(ReservationStatus.REQUESTED);
    }

    public void updateStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public boolean isClient(String userNo) {
        return this.userNo.equals(userNo);
    }
}
