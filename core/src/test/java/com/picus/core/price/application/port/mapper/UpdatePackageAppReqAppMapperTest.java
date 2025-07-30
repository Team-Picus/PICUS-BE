package com.picus.core.price.application.port.mapper;

import com.picus.core.price.application.port.in.mapper.UpdatePackageAppMapper;
import com.picus.core.price.application.port.in.request.UpdatePackageCommand;
import com.picus.core.price.domain.Package;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UpdatePackageAppReqAppMapperTest {
    private final UpdatePackageAppMapper appMapper = new UpdatePackageAppMapper();

    @Test
    @DisplayName("UpdatePackageCommand -> Package 매핑")
    public void toDomain() throws Exception {
        // given
        UpdatePackageCommand command = UpdatePackageCommand.builder()
                .packageNo("pkg01")
                .name("pkg_name")
                .price(10000)
                .contents(List.of("cont1"))
                .notice("notice")
                .build();

        // when
        Package domain = appMapper.toDomain(command);

        // then
        assertThat(domain.getPackageNo()).isEqualTo("pkg01");
        assertThat(domain.getName()).isEqualTo("pkg_name");
        assertThat(domain.getPrice()).isEqualTo(10000);
        assertThat(domain.getContents()).isEqualTo(List.of("cont1"));
        assertThat(domain.getNotice()).isEqualTo("notice");
    }

}