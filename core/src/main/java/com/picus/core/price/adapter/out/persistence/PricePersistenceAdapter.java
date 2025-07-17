package com.picus.core.price.adapter.out.persistence;

import com.picus.core.price.adapter.out.persistence.entity.OptionEntity;
import com.picus.core.price.adapter.out.persistence.entity.PackageEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import com.picus.core.price.adapter.out.persistence.mapper.OptionPersistenceMapper;
import com.picus.core.price.adapter.out.persistence.mapper.PackagePersistenceMapper;
import com.picus.core.price.adapter.out.persistence.mapper.PricePersistenceMapper;
import com.picus.core.price.adapter.out.persistence.repository.OptionJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PackageJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceJpaRepository;
import com.picus.core.price.application.port.out.LoadPricePort;
import com.picus.core.price.domain.model.Option;
import com.picus.core.price.domain.model.Package;
import com.picus.core.price.domain.model.Price;
import com.picus.core.shared.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class PricePersistenceAdapter implements LoadPricePort {

    private final PriceJpaRepository priceJpaRepository;
    private final PackageJpaRepository packageJpaRepository;
    private final OptionJpaRepository optionJpaRepository;

    private final PricePersistenceMapper pricePersistenceMapper;
    private final PackagePersistenceMapper packagePersistenceMapper;
    private final OptionPersistenceMapper optionPersistenceMapper;

    @Override
    public List<Price> findByExpertNo(String expertNo) {
        List<Price> result = new ArrayList<>();
        List<PriceEntity> priceEntities = priceJpaRepository.findByExpertNo(expertNo);

        for (PriceEntity priceEntity : priceEntities) {
            List<Package> packages = packageJpaRepository.findByPriceEntity(priceEntity).stream()
                    .map(packagePersistenceMapper::toDomain)
                    .toList();
            List<Option> options = optionJpaRepository.findByPriceEntity(priceEntity).stream()
                    .map(optionPersistenceMapper::toDomain)
                    .toList();
            result.add(pricePersistenceMapper.toDomain(priceEntity, packages, options));
        }

        return result;
    }
}
