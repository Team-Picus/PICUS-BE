package com.picus.core.price.adapter.out.persistence;

import com.picus.core.price.adapter.out.persistence.entity.PriceReferenceImageEntity;
import com.picus.core.price.domain.model.PriceReferenceImage;
import org.springframework.stereotype.Component;

@Component
public class PriceReferenceImagePersistenceMapper {

    public PriceReferenceImage toDomain(PriceReferenceImageEntity entity) {
        return PriceReferenceImage.builder()
                .fileKey(entity.getFileKey())
                .imageOrder(entity.getImageOrder())
                .build();
    }
}
