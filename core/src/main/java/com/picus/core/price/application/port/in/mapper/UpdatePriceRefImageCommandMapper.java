package com.picus.core.price.application.port.in.mapper;

import com.picus.core.price.application.port.in.command.UpdatePriceReferenceImageCommand;
import com.picus.core.price.domain.PriceReferenceImage;
import org.springframework.stereotype.Component;

@Component
public class UpdatePriceRefImageCommandMapper {

    public PriceReferenceImage toDomain(UpdatePriceReferenceImageCommand command) {
        return PriceReferenceImage.builder()
                .priceRefImageNo(command.priceRefImageNo())
                .fileKey(command.fileKey())
                .imageOrder(command.imageOrder())
                .build();
    }
}
