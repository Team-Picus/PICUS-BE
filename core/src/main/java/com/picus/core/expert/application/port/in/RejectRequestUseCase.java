package com.picus.core.expert.application.port.in;

/**
 * 전문가 요청 거절 Use case
 * - Expert의 Approval Status를 REJECT로 변경한다.
 */
public interface RejectRequestUseCase {

    void rejectRequest(String expertNo);
}
