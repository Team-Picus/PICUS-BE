package com.picus.core.price.adapter.out.persistence.mapper;

import com.picus.core.price.adapter.out.persistence.entity.OptionEntity;
import com.picus.core.price.adapter.out.persistence.entity.PackageEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import com.picus.core.price.domain.model.Option;
import com.picus.core.price.domain.model.Package;
import com.picus.core.price.domain.model.Price;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PricePersistenceMapper {

    public Price toDomain(PriceEntity priceEntity, List<Package> packages, List<Option> options) {

        return Price.builder()
                .priceNo(priceEntity.getPriceNo())
                .priceThemeType(priceEntity.getPriceThemeType())
                .packages(packages)
                .options(options)
                .build();
    }
}
