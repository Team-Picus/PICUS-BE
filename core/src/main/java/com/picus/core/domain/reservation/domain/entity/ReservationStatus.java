package com.picus.core.domain.reservation.domain.entity;

public enum ReservationStatus {
    DRAFT, // 임시저장
    CHECKING,   // 예약 확인 상태 (클라이언트가 전문가에게 예약 요청)
    APPROVAL,   // 예약 수락 상태 (전문가가 해당 요청 확인 후 수락)
    COMPLETED_PAYMENTS,  // 선입금 완료 상태 (수락된 예약금 지불 후 상태)
    IN_PROGRESS,    // 촬영 및 편집 진행 중
    COMPLETED,  // 스냅/편집 완료
    CANCELING,  // 예약 취소(환불) 진행 중
    CANCELED    // 예약 취소(환불) 완료
}
