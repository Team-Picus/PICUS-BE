package com.picus.core.price.application.port.mapper;

import com.picus.core.price.application.port.in.command.PriceReferenceImageCommand;
import com.picus.core.price.domain.model.PriceReferenceImage;
import org.springframework.stereotype.Component;

@Component
public class PriceRefImageCommandAppMapper {

    public PriceReferenceImage toDomain(PriceReferenceImageCommand command) {
        return PriceReferenceImage.builder()
                .priceRefImageNo(command.priceRefImageNo())
                .fileKey(command.fileKey())
                .imageOrder(command.imageOrder())
                .build();
    }
}
