package com.picus.core.price.adapter.out.persistence.mapper;

import com.picus.core.price.adapter.out.persistence.entity.PriceReferenceImageEntity;
import com.picus.core.price.domain.PriceReferenceImage;
import org.springframework.stereotype.Component;

@Component
public class PriceReferenceImagePersistenceMapper {

    public PriceReferenceImage toDomain(PriceReferenceImageEntity entity) {
        return PriceReferenceImage.builder()
                .priceRefImageNo(entity.getPriceReferenceImageNo())
                .fileKey(entity.getFileKey())
                .imageOrder(entity.getImageOrder())
                .build();
    }

    public PriceReferenceImageEntity toEntity(PriceReferenceImage refImage) {
        return PriceReferenceImageEntity.builder()
                .fileKey(refImage.getFileKey())
                .imageOrder(refImage.getImageOrder())
                .build();
    }
}
