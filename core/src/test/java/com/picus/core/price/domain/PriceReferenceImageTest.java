package com.picus.core.price.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PriceReferenceImageTest {

    @Test
    @DisplayName("PriceReferenceImage의 정보를 업데이트 한다.")
    public void updatePriceReferenceImage() throws Exception {
        // given
        PriceReferenceImage priceReferenceImage = PriceReferenceImage.builder()
                .priceRefImageNo("ref_no")
                .fileKey("file_key")
                .imageOrder(1)
                .build();

        // when
        priceReferenceImage.updatePriceReferenceImage("new_file_key", 2);

        // then
        assertThat(priceReferenceImage.getFileKey()).isEqualTo("new_file_key");
        assertThat(priceReferenceImage.getImageOrder()).isEqualTo(2);
    }

}