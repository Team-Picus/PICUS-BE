package com.picus.core.price.adapter.out.persistence;

import com.picus.core.price.adapter.out.persistence.entity.OptionEntity;
import com.picus.core.price.adapter.out.persistence.entity.PackageEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceReferenceImageEntity;
import com.picus.core.price.adapter.out.persistence.mapper.OptionPersistenceMapper;
import com.picus.core.price.adapter.out.persistence.mapper.PackagePersistenceMapper;
import com.picus.core.price.adapter.out.persistence.mapper.PricePersistenceMapper;
import com.picus.core.price.adapter.out.persistence.mapper.PriceReferenceImagePersistenceMapper;
import com.picus.core.price.adapter.out.persistence.repository.OptionJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PackageJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceReferenceImageJpaRepository;
import com.picus.core.price.application.port.out.PriceCommandPort;
import com.picus.core.price.application.port.out.PriceQueryPort;
import com.picus.core.price.domain.model.Option;
import com.picus.core.price.domain.model.Package;
import com.picus.core.price.domain.model.Price;
import com.picus.core.price.domain.model.PriceReferenceImage;
import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.shared.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class PricePersistenceAdapter implements PriceQueryPort, PriceCommandPort {

    private final PriceJpaRepository priceJpaRepository;
    private final PackageJpaRepository packageJpaRepository;
    private final OptionJpaRepository optionJpaRepository;
    private final PriceReferenceImageJpaRepository priceReferenceImageJpaRepository;

    private final PricePersistenceMapper pricePersistenceMapper;
    private final PackagePersistenceMapper packagePersistenceMapper;
    private final OptionPersistenceMapper optionPersistenceMapper;
    private final PriceReferenceImagePersistenceMapper priceReferenceImagePersistenceMapper;

    @Override
    public List<Price> findByExpertNo(String expertNo) {
        List<Price> result = new ArrayList<>();
        List<PriceEntity> priceEntities = priceJpaRepository.findByExpertNo(expertNo);

        for (PriceEntity priceEntity : priceEntities) {
            List<Package> packages = findPackagesByPriceNo(priceEntity); // 해당 Price의 Package 불러오기

            List<Option> options = findOptionsByPriceNo(priceEntity); // 해당 Price의 Option 불러오기

            List<PriceReferenceImage> referenceImages = findPriceRefImagesByPriceNo(priceEntity); // 해당 Price의 RefImage 불러오기

            result.add(pricePersistenceMapper.toDomain(priceEntity, packages, options, referenceImages));
        }

        return result;
    }

    @Override
    public Price findById(String priceNo) {
        PriceEntity priceEntity = priceJpaRepository.findById(priceNo)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        List<Package> packages = findPackagesByPriceNo(priceEntity); // 해당 Price의 Package 불러오기

        List<Option> options = findOptionsByPriceNo(priceEntity); // 해당 Price의 Option 불러오기

        List<PriceReferenceImage> referenceImages = findPriceRefImagesByPriceNo(priceEntity); // 해당 Price의 RefImage 불러오기

        return pricePersistenceMapper.toDomain(priceEntity, packages, options, referenceImages);
    }

    @Override
    public Price save(Price price, String expertNo) {

        // PriceEntity 저장
        PriceEntity savedPriceEntity = savePriceEntity(price, expertNo);

        // PackageEntity 저장
        List<Package> packages = savePackageEntities(savedPriceEntity, price.getPackages());

        // OptionEntity 저장
        List<Option> options = saveOptionEntities(savedPriceEntity, price.getOptions());

        // PriceRefImageEntity 저장
        List<PriceReferenceImage> referenceImages = savePriceRefImageEntities(savedPriceEntity, price.getPriceReferenceImages());

        return pricePersistenceMapper.toDomain(savedPriceEntity, packages, options, referenceImages);
    }

    @Override
    public void delete(String priceNo) {
        priceJpaRepository.deleteById(priceNo);

        priceReferenceImageJpaRepository.deleteByPriceEntity_PriceNo(priceNo);
        packageJpaRepository.deleteByPriceEntity_PriceNo(priceNo);
        optionJpaRepository.deleteByPriceEntity_PriceNo(priceNo);
    }

    @Override
    public void update(Price price) {

    }

    /**
     * private 메서드
     */
    private List<Package> findPackagesByPriceNo(PriceEntity priceEntity) {
        return packageJpaRepository.findByPriceEntity(priceEntity).stream()
                .map(packagePersistenceMapper::toDomain)
                .toList();
    }

    private List<Option> findOptionsByPriceNo(PriceEntity priceEntity) {
        return optionJpaRepository.findByPriceEntity(priceEntity).stream()
                .map(optionPersistenceMapper::toDomain)
                .toList();
    }

    private List<PriceReferenceImage> findPriceRefImagesByPriceNo(PriceEntity priceEntity) {
        return priceReferenceImageJpaRepository.findByPriceEntity(priceEntity).stream()
                .map(priceReferenceImagePersistenceMapper::toDomain)
                .toList();
    }

    private PriceEntity savePriceEntity(Price price, String expertNo) {
        PriceEntity entity = pricePersistenceMapper.toEntity(price);
        entity.bindExpertNo(expertNo);
        return priceJpaRepository.save(entity);
    }

    private List<Package> savePackageEntities(PriceEntity savedPriceEntity, List<Package> packages) {
        List<PackageEntity> packageEntities = packages.stream()
                .map(p -> {
                    PackageEntity packageEntity = packagePersistenceMapper.toEntity(p);
                    packageEntity.assignPriceEntity(savedPriceEntity);
                    return packageEntity;
                }).toList();
        return packageJpaRepository.saveAll(packageEntities).stream()
                .map(packagePersistenceMapper::toDomain)
                .toList();
    }

    private List<Option> saveOptionEntities(PriceEntity savedPriceEntity, List<Option> options) {
        List<OptionEntity> optionEntities = options.stream()
                .map(option -> {
                    OptionEntity optionEntity = optionPersistenceMapper.toEntity(option);
                    optionEntity.assignPriceEntity(savedPriceEntity);
                    return optionEntity;
                }).toList();
        return optionJpaRepository.saveAll(optionEntities).stream()
                .map(optionPersistenceMapper::toDomain)
                .toList();
    }

    private List<PriceReferenceImage> savePriceRefImageEntities(PriceEntity savedPriceEntity, List<PriceReferenceImage> referenceImages) {
        List<PriceReferenceImageEntity> priceReferenceImageEntities  = referenceImages.stream()
                .map(refImage -> {
                    PriceReferenceImageEntity priceRefImageEntity = priceReferenceImagePersistenceMapper.toEntity(refImage);
                    priceRefImageEntity.assignPriceEntity(savedPriceEntity);
                    return priceRefImageEntity;
                }).toList();
        return priceReferenceImageJpaRepository.saveAll(priceReferenceImageEntities).stream()
                .map(priceReferenceImagePersistenceMapper::toDomain)
                .toList();
    }
}
