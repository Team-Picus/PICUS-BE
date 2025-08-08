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
    private PriceThemeType priceThemeType;
    private String requestDetail;
    private SelectedPackage selectedPackage;
    private List<SelectedOption> selectedOptions;
    private Integer totalPrice;

    // FK
    private String userNo;
    private String expertNo;
}
