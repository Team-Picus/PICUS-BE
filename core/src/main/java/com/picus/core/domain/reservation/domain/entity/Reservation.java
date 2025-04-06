package com.picus.core.domain.reservation.domain.entity;

import com.picus.core.domain.reservation.domain.entity.order.SelectedOption;
import com.picus.core.global.common.base.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id @Tsid
    @Column(name = "reservation_no")
    private Long id;

    // 예약 정보(시간, 장소)
    private Schedule schedule;

    // 사용자의 요구 사항
    private String detail;

    // 예약 상태
    private ReservationStatus status = ReservationStatus.DRAFT;

    private Long clientId;

    // 포스트 번호 (캡처)
    @Column(nullable = false)
    private Long postId;

    // 사용자가 선택한 옵션
    @OneToOne(mappedBy = "reservation",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private SelectedOption selectedOption;

    @OneToMany(mappedBy = "reservation",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<AdditionalFee> additionalFees = new ArrayList<>();

    // ======================================
    // =            Constructors            =
    // ======================================
    /**
     * 사용자가 예약 정보를 입력하기 전에 생성되는 예약 정보
     * @param clientId 사용자 번호
     * @param postId 포스트 번호
     * @param basicPrice 기본 가격
     */
    public Reservation(Long clientId, Long postId, Integer basicPrice) {
        this.clientId = clientId;
        this.postId = postId;
        this.selectedOption = new SelectedOption(this, basicPrice);
    }

    // ======================================
    // =          Business methods          =
    // ======================================
    /**
     * 사용자의 요청을 받아 예약을 등록한다.
     * @param schedule 예약 정보
     * @param detail 예약 상세 정보
     */
    public void register(Schedule schedule, String detail) {
        this.schedule = schedule;
        this.detail = detail;
        this.status = ReservationStatus.CHECKING;
    }

    /**
     * 사용자가 예약 정보를 수정한다. (예약 정보 수정은 예약 상태가 CHECKING 일 때만 가능하다.)
     * @param optionNo 변경할 옵션 번호
     * @param count 변경할 옵션 개수
     */
    public void addAdditionalOption(Long optionNo, int count) {
        if (status != ReservationStatus.CHECKING) {
            throw new IllegalStateException("예약 상태가 CHECKING이 아닙니다.");
        }
        selectedOption.addAdditionalOption(optionNo, count);
    }

    /**
     * 사용자가 추가한 옵션을 변경한다. (예약 정보 수정은 예약 상태가 CHECKING 일 때만 가능하다.)
     * @param optionNo 변경할 옵션 번호
     * @param count 변경할 옵션 개수
     */
    public void changeAdditionalOption(Long optionNo, int count) {
        if (status != ReservationStatus.CHECKING) {
            throw new IllegalStateException("예약 상태가 CHECKING이 아닙니다.");
        }
        selectedOption.changeAdditionalOption(optionNo, count);
    }

    public void addAdditionalFee(Integer price, String detail) {
        AdditionalFee additionalFee = new AdditionalFee(this, price, detail);
        additionalFees.add(additionalFee);
    }

}
