package com.picus.core.price.domain.model;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class PriceReferenceImage {

    private String priceRefImageNo;

    private String fileKey;
    private String imageUrl;
    private Integer imageOrder;

    public void updatePriceReferenceImage(String fileKey, Integer imageOrder) {
        if(fileKey != null)
            this.fileKey = fileKey;
        if(imageOrder != null)
            this.imageOrder = imageOrder;
    }
}
