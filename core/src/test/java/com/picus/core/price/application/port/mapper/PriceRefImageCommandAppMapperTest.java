package com.picus.core.price.application.port.mapper;

import com.picus.core.price.application.port.in.request.ChangeStatus;
import com.picus.core.price.application.port.in.request.UpdatePriceReferenceImageAppReq;
import com.picus.core.price.domain.model.PriceReferenceImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PriceRefImageCommandAppMapperTest {

    PriceRefImageCommandAppMapper appMapper = new PriceRefImageCommandAppMapper();

    @Test
    @DisplayName("UpdatePriceReferenceImageAppReq -> PriceReferenceImage 매핑")
    public void toDomain() throws Exception {
        // given
        UpdatePriceReferenceImageAppReq command = UpdatePriceReferenceImageAppReq.builder()
                .priceRefImageNo("ref1")
                .fileKey("file_key")
                .imageOrder(1)
                .status(ChangeStatus.UPDATE)
                .build();

        // when
        PriceReferenceImage domain = appMapper.toDomain(command);

        // then
        assertThat(domain.getPriceRefImageNo()).isEqualTo("ref1");
        assertThat(domain.getFileKey()).isEqualTo("file_key");
        assertThat(domain.getImageOrder()).isEqualTo(1);
    }

}