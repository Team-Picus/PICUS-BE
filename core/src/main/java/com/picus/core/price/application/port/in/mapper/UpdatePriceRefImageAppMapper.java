package com.picus.core.price.application.port.in.mapper;

import com.picus.core.price.application.port.in.request.UpdatePriceReferenceImageAppReq;
import com.picus.core.price.domain.PriceReferenceImage;
import org.springframework.stereotype.Component;

@Component
public class UpdatePriceRefImageAppMapper {

    public PriceReferenceImage toDomain(UpdatePriceReferenceImageAppReq command) {
        return PriceReferenceImage.builder()
                .priceRefImageNo(command.priceRefImageNo())
                .fileKey(command.fileKey())
                .imageOrder(command.imageOrder())
                .build();
    }
}
