package com.picus.core.expert.application.port.in;

/**
 * 전문가 요청 수락 Use case
 * - Expert의 Approval Status를 Approval로 변경한다.
 */
public interface ApproveRequestUseCase {

    void approveRequest(String expertNo);
}
