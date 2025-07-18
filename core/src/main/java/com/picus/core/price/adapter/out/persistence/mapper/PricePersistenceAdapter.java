package com.picus.core.price.adapter.out.persistence.mapper;

import com.picus.core.price.adapter.out.persistence.PriceReferenceImagePersistenceMapper;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import com.picus.core.price.adapter.out.persistence.repository.OptionJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PackageJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceReferenceImageJpaRepository;
import com.picus.core.price.application.port.out.LoadPricePort;
import com.picus.core.price.domain.model.Option;
import com.picus.core.price.domain.model.Package;
import com.picus.core.price.domain.model.Price;
import com.picus.core.price.domain.model.PriceReferenceImage;
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
    private final PriceReferenceImageJpaRepository priceReferenceImageJpaRepository;
    ;

    private final PricePersistenceMapper pricePersistenceMapper;
    private final PackagePersistenceMapper packagePersistenceMapper;
    private final OptionPersistenceMapper optionPersistenceMapper;
    private final PriceReferenceImagePersistenceMapper priceReferenceImagePersistenceMapper;

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
            List<PriceReferenceImage> referenceImages = priceReferenceImageJpaRepository.findByPriceEntity(priceEntity).stream()
                    .map(priceReferenceImagePersistenceMapper::toDomain)
                    .toList();
            result.add(pricePersistenceMapper.toDomain(priceEntity, packages, options, referenceImages));
        }

        return result;
    }
}
