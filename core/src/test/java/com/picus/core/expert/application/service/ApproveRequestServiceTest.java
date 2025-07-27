package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.out.ReadExpertPort;
import com.picus.core.expert.application.port.out.CreateExpertPort;
import com.picus.core.expert.application.port.out.UpdateExpertPort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.expert.domain.vo.ApprovalStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class ApproveRequestServiceTest {

    private final ReadExpertPort readExpertPort = Mockito.mock(ReadExpertPort.class);
    private final UpdateExpertPort updateExpertPort = Mockito.mock(UpdateExpertPort.class);

    private final ApproveRequestService approveRequestService =
            new ApproveRequestService(readExpertPort, updateExpertPort);


    @Test
    @DisplayName("승인을 수락하면 Expert의 approvalStatus가 APPROVAL로 변경된다.")
    public void approveRequest_success() throws Exception {
        // given
        String expertNo = "expertNo1";
        Expert expert = stubLoadExpertPortResult(expertNo);


        // when
        approveRequestService.approveRequest(expertNo);

        // then
        assertThat(expert.getApprovalStatus())
                .isEqualTo(ApprovalStatus.APPROVAL);

        then(readExpertPort).should()
                .findById(eq(expertNo));
        then(updateExpertPort).should()
                .update(eq(expert));
    }

    private Expert stubLoadExpertPortResult(String expertNo) {
        Expert expert = Expert.builder()
                .expertNo(expertNo)
                .approvalStatus(ApprovalStatus.PENDING)
                .build();
        given(readExpertPort.findById(expertNo))
                .willReturn(Optional.of(expert));
        return expert;
    }

}