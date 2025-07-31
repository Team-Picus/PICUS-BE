package com.picus.core.price.adapter.out.persistence.mapper;

import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import com.picus.core.price.domain.Option;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.PriceReferenceImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PricePersistenceMapper {

    public Price toDomain(
            PriceEntity priceEntity,
            List<Package> packages,
            List<Option> options,
            List<PriceReferenceImage> referenceImages
    ) {

        return Price.builder()
                .priceNo(priceEntity.getPriceNo())
                .expertNo(priceEntity.getExpertNo())
                .priceThemeType(priceEntity.getPriceThemeType())
                .snapSubTheme(priceEntity.getSnapSubTheme())
                .packages(packages)
                .options(options)
                .priceReferenceImages(referenceImages)
                .build();
    }

    public PriceEntity toEntity(Price price, String expertNo) {
        return PriceEntity.builder()
                .expertNo(expertNo)
                .priceThemeType(price.getPriceThemeType())
                .snapSubTheme(price.getSnapSubTheme())
                .build();
    }
}
