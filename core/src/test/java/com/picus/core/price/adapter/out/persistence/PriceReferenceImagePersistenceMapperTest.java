package com.picus.core.price.adapter.out.persistence;

import static org.junit.jupiter.api.Assertions.*;

import com.picus.core.price.adapter.out.persistence.entity.PriceReferenceImageEntity;
import com.picus.core.price.domain.model.PriceReferenceImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PriceReferenceImagePersistenceMapperTest {

    private final PriceReferenceImagePersistenceMapper mapper = new PriceReferenceImagePersistenceMapper();

    @Test
    @DisplayName("PriceReferenceImageEntity를 도메인 모델로 매핑한다")
    void toDomain_shouldMapCorrectly() {
        // given
        PriceReferenceImageEntity entity = PriceReferenceImageEntity.builder()
                .priceReferenceImageNo("img_no")
                .fileKey("file-key-123")
                .imageOrder(1)
                .build();

        // when
        PriceReferenceImage domain = mapper.toDomain(entity);

        // then
        assertThat(domain.getPriceRefImageNo()).isEqualTo("img_no");
        assertThat(domain.getFileKey()).isEqualTo("file-key-123");
        assertThat(domain.getImageOrder()).isEqualTo(1);
    }
}