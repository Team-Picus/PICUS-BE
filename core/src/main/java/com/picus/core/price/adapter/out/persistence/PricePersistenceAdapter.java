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
import com.picus.core.price.domain.Option;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.PriceReferenceImage;
import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.shared.exception.RestApiException;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

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
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

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
        // 자식 먼저 삭제
        priceReferenceImageJpaRepository.deleteByPriceEntity_PriceNo(priceNo);
        packageJpaRepository.deleteByPriceEntity_PriceNo(priceNo);
        optionJpaRepository.deleteByPriceEntity_PriceNo(priceNo);
        // 부모 삭제
        priceJpaRepository.deleteById(priceNo);
    }

    @Override
    public void update(Price price,
                       List<String> deletedPriceRefImageNos, List<String> deletedPackageNos, List<String> deletedOptionNos
    ) {
        PriceEntity priceEntity = priceJpaRepository.findById(price.getPriceNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        priceEntity.updateEntity(price.getPriceThemeType());

        // PricePreference 추가/수정/삭제
        updatePriceReferenceImageEntities(price, deletedPriceRefImageNos, priceEntity);

        // Package 추가/수정/삭제
        updatePackageEntities(price, deletedPackageNos, priceEntity);

        // Option 추가/수정/삭제
        updateOptionEntities(price, deletedOptionNos, priceEntity);
    }

    /**
     * private 메서드
     */
    private List<Package> findPackagesByPriceNo(PriceEntity priceEntity) {
        return packageJpaRepository.findByPriceEntity_PriceNo(priceEntity.getPriceNo()).stream()
                .map(packagePersistenceMapper::toDomain)
                .toList();
    }

    private List<Option> findOptionsByPriceNo(PriceEntity priceEntity) {
        return optionJpaRepository.findByPriceEntity_PriceNo(priceEntity.getPriceNo()).stream()
                .map(optionPersistenceMapper::toDomain)
                .toList();
    }

    private List<PriceReferenceImage> findPriceRefImagesByPriceNo(PriceEntity priceEntity) {
        return priceReferenceImageJpaRepository.findByPriceEntity_PriceNo(priceEntity.getPriceNo()).stream()
                .map(priceReferenceImagePersistenceMapper::toDomain)
                .toList();
    }

    private PriceEntity savePriceEntity(Price price, String expertNo) {
        PriceEntity entity = pricePersistenceMapper.toEntity(price, expertNo);
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

    private void updatePackageEntities(Price price, List<String> deletedPackageNos, PriceEntity priceEntity) {
        // 삭제
        packageJpaRepository.deleteByPackageNoIn(deletedPackageNos);

        // 추가/수정
        List<Package> packages = price.getPackages();
        for (Package domain : packages) {
            String packageNo = domain.getPackageNo();
            if(packageNo != null) {
                // PK가 있다 = 수정
                PackageEntity entity = packageJpaRepository.findById(packageNo)
                        .orElseThrow(() -> new RestApiException(_NOT_FOUND));
                entity.updateEntity(domain.getName(), domain.getPrice(), domain.getContents(), domain.getNotice());
            } else {
                // PK가 없다 = 저장
                PackageEntity entity = packagePersistenceMapper.toEntity(domain);
                entity.assignPriceEntity(priceEntity);
                packageJpaRepository.save(entity);
            }
        }
    }

    private void updatePriceReferenceImageEntities(Price price, List<String> deletedPriceRefImageNos, PriceEntity priceEntity) {
        // 삭제
        priceReferenceImageJpaRepository.deleteByPriceReferenceImageNoIn(deletedPriceRefImageNos);

        // 삭제 후 이미지 순서 재정렬
        List<PriceReferenceImageEntity> images =
                priceReferenceImageJpaRepository.findAllByPriceEntity_PriceNoOrderByImageOrder(priceEntity.getPriceNo());

        for (int i = 0; i < images.size(); i++) {
            images.get(i).assignImageOrder(i + 1);
        }

        // 추가/수정
        priceReferenceImageJpaRepository.shiftAllImageOrdersToNegative(priceEntity.getPriceNo()); // 추가/수정 전에 모든 imageOrder 값을 임시값으로 변경

        List<PriceReferenceImage> priceReferenceImages = price.getPriceReferenceImages();
        for (PriceReferenceImage domain : priceReferenceImages) {
            String priceRefImageNo = domain.getPriceRefImageNo();
            if(priceRefImageNo != null) {
                // PK가 있다 = 수정
                PriceReferenceImageEntity entity = priceReferenceImageJpaRepository.findById(priceRefImageNo)
                        .orElseThrow(() -> new RestApiException(_NOT_FOUND));
                entity.updateEntity(domain.getFileKey(), domain.getImageOrder());
            } else {
                // PK가 없다 = 저장
                PriceReferenceImageEntity entity = priceReferenceImagePersistenceMapper.toEntity(domain);
                entity.assignPriceEntity(priceEntity);
                priceReferenceImageJpaRepository.save(entity);
            }
        }

        // 최종적으로 재정렬
        List<PriceReferenceImageEntity> finalImages =
                priceReferenceImageJpaRepository.findAllByPriceEntity_PriceNoOrderByImageOrder(priceEntity.getPriceNo());

        for (int i = 0; i < finalImages.size(); i++) {
            finalImages.get(i).assignImageOrder(i + 1);
        }
    }

    private void updateOptionEntities(Price price, List<String> deletedOptionNos, PriceEntity priceEntity) {
        // 삭제
        optionJpaRepository.deleteByOptionNoIn(deletedOptionNos);

        // 추가/수정
        List<Option> options = price.getOptions();
        for (Option domain : options) {
            String optionNo = domain.getOptionNo();
            if(optionNo != null) {
                // PK가 있다 = 수정
                OptionEntity entity = optionJpaRepository.findById(optionNo)
                        .orElseThrow(() -> new RestApiException(_NOT_FOUND));
                entity.updateEntity(domain.getName(), domain.getCount(), domain.getPrice(), domain.getContents());
            } else {
                // PK가 없다 = 저장
                OptionEntity entity = optionPersistenceMapper.toEntity(domain);
                entity.assignPriceEntity(priceEntity);
                optionJpaRepository.save(entity);
            }
        }
    }
}
