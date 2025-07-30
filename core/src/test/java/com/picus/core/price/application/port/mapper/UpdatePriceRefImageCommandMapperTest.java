package com.picus.core.price.application.port.mapper;

import com.picus.core.price.application.port.in.mapper.UpdatePriceRefImageCommandMapper;
import com.picus.core.price.application.port.in.command.ChangeStatus;
import com.picus.core.price.application.port.in.command.UpdatePriceReferenceImageCommand;
import com.picus.core.price.domain.PriceReferenceImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UpdatePriceRefImageCommandMapperTest {

    UpdatePriceRefImageCommandMapper appMapper = new UpdatePriceRefImageCommandMapper();

    @Test
    @DisplayName("UpdatePriceReferenceImageCommand -> PriceReferenceImage 매핑")
    public void toDomain() throws Exception {
        // given
        UpdatePriceReferenceImageCommand command = UpdatePriceReferenceImageCommand.builder()
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