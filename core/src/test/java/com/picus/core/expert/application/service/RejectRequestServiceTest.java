package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.out.LoadExpertPort;
import com.picus.core.expert.application.port.out.UpdateExpertPort;
import com.picus.core.expert.domain.model.Expert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class RejectRequestServiceTest {
    private final LoadExpertPort loadExpertPort = Mockito.mock(LoadExpertPort.class);
    private final UpdateExpertPort updateExpertPort = Mockito.mock(UpdateExpertPort.class);

    private final RejectRequestService rejectRequestService =
            new RejectRequestService(loadExpertPort, updateExpertPort);


    @Test
    @DisplayName("승인을 거절하면 Expert의 approvalStatus가 REJECT로 변경된다.")
    public void rejectRequest_success() throws Exception {
        // given
        String expertNo = "expertNo1";
        Expert expert = stubLoadExpertByExpertNo(expertNo);


        // when
        rejectRequestService.rejectRequest(expertNo);

        // then
        then(expert).should()
                .rejectApprovalRequest();
        then(loadExpertPort).should()
                .findById(eq(expertNo));
        then(updateExpertPort).should()
                .updateExpert(eq(expert));
    }

    private Expert stubLoadExpertByExpertNo(String expertNo) {
        Expert expert = Mockito.mock(Expert.class);
        given(loadExpertPort.findById(expertNo))
                .willReturn(Optional.of(expert));
        return expert;
    }
}