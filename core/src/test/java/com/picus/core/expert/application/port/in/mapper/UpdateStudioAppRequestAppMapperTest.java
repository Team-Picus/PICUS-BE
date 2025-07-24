package com.picus.core.expert.application.port.in.mapper;

import com.picus.core.expert.application.port.in.request.ChangeStatus;
import com.picus.core.expert.application.port.in.request.UpdateStudioAppReq;
import com.picus.core.expert.domain.model.Studio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateStudioAppRequestAppMapperTest {

    private final UpdateStudioAppMapper mapper = new UpdateStudioAppMapper();

    @Test
    @DisplayName("StudioCommand를 Studio 도메인으로 매핑한다.")
    void toDomain_success() {
        // given
        UpdateStudioAppReq command = UpdateStudioAppReq.builder()
                .studioNo("ST-001")
                .studioName("픽어스 스튜디오")
                .employeesCount(5)
                .businessHours("10:00~19:00")
                .address("서울특별시 강남구")
                .changeStatus(ChangeStatus.NEW)
                .build();

        // when
        Studio result = mapper.toDomain(command);

        // then
        assertThat(result.getStudioNo()).isEqualTo("ST-001");
        assertThat(result.getStudioName()).isEqualTo("픽어스 스튜디오");
        assertThat(result.getEmployeesCount()).isEqualTo(5);
        assertThat(result.getBusinessHours()).isEqualTo("10:00~19:00");
        assertThat(result.getAddress()).isEqualTo("서울특별시 강남구");
    }
}