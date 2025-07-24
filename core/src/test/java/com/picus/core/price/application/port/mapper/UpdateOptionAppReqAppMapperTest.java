package com.picus.core.price.application.port.mapper;

import com.picus.core.price.application.port.in.request.UpdateOptionAppReq;
import com.picus.core.price.domain.model.Option;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class UpdateOptionAppReqAppMapperTest {

    private final OptionCommandAppMapper appMapper = new OptionCommandAppMapper();

    @Test
    @DisplayName("UpdateOptionAppReq -> Option 매핑")
    public void toDomain() throws Exception {
        // given
        UpdateOptionAppReq command = UpdateOptionAppReq.builder()
                .optionNo("opt_no")
                .name("opt_name")
                .count(3)
                .price(10000)
                .contents(List.of("content1"))
                .build();

        // when
        Option domain = appMapper.toDomain(command);

        // then
        Assertions.assertThat(domain)
                .extracting(
                        Option::getOptionNo,
                        Option::getName,
                        Option::getCount,
                        Option::getPrice,
                        Option::getContents
                )
                .containsExactly(
                        "opt_no", "opt_name", 3, 10000, List.of("content1")
                );
    }

}