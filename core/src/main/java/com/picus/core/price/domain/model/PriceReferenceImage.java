package com.picus.core.price.domain.model;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class PriceReferenceImage {
    private String fileKeys;
    private Integer imageOrder;
}
