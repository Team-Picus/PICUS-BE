package com.picus.core.domain.reservation.domain.service;

import com.picus.core.domain.reservation.domain.entity.Reservation;
import com.picus.core.domain.reservation.domain.entity.Schedule;
import com.picus.core.domain.reservation.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public Reservation findById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + reservationId));
    }
    /**
     * 새로운 Reservation을 생성하여 저장합니다.
     * 이때 기본 가격을 기반으로 SelectedOption이 함께 생성됩니다.
     */
    @Transactional
    public Reservation createReservation(Long clientId, Long postId, Integer basicPrice) {
        Reservation reservation = new Reservation(clientId, postId, basicPrice);
        return reservationRepository.save(reservation);
    }

    /**
     * 이미 생성된 Reservation에 대해 예약 등록을 진행합니다.
     * 예약 등록은 스케줄, 상세 정보를 설정하고 상태를 CHECKING으로 변경합니다.
     */
    @Transactional
    public Reservation registerReservation(Long reservationId, Schedule schedule, String detail) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + reservationId));
        reservation.register(schedule, detail);
        return reservationRepository.save(reservation);
    }

    /**
     * 예약이 CHECKING 상태일 때 추가 옵션을 추가합니다.
     */
    @Transactional
    public Reservation addAdditionalOption(Long reservationId, Long optionNo, int count) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + reservationId));
        reservation.addAdditionalOption(optionNo, count);
        return reservationRepository.save(reservation);
    }

    /**
     * 예약이 CHECKING 상태일 때 이미 추가된 옵션을 변경합니다.
     */
    @Transactional
    public Reservation changeAdditionalOption(Long reservationId, Long optionNo, int count) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + reservationId));
        reservation.changeAdditionalOption(optionNo, count);
        return reservationRepository.save(reservation);
    }

    /**
     * 예약에 추가 요금을 등록합니다.
     */
    @Transactional
    public Reservation addAdditionalFee(Long reservationId, Integer feePrice, String feeDetail) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + reservationId));
        reservation.addAdditionalFee(feePrice, feeDetail);
        return reservationRepository.save(reservation);
    }
}
