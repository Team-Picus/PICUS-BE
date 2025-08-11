package com.picus.core.price.adapter.out.persistence;

import com.picus.core.price.adapter.out.persistence.entity.*;
import com.picus.core.price.adapter.out.persistence.mapper.OptionPersistenceMapper;
import com.picus.core.price.adapter.out.persistence.mapper.PackagePersistenceMapper;
import com.picus.core.price.adapter.out.persistence.mapper.PricePersistenceMapper;
import com.picus.core.price.adapter.out.persistence.mapper.PriceReferenceImagePersistenceMapper;
import com.picus.core.price.adapter.out.persistence.repository.OptionJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PackageJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceReferenceImageJpaRepository;
import com.picus.core.price.application.port.out.PriceCreatePort;
import com.picus.core.price.application.port.out.PriceDeletePort;
import com.picus.core.price.application.port.out.PriceReadPort;
import com.picus.core.price.application.port.out.PriceUpdatePort;
import com.picus.core.price.domain.Option;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.PriceReferenceImage;
import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.domain.vo.SnapSubTheme;
import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.shared.exception.RestApiException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.picus.core.price.adapter.out.persistence.entity.QPriceEntity.priceEntity;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@PersistenceAdapter
@RequiredArgsConstructor
public class PricePersistenceAdapter implements PriceCreatePort, PriceReadPort, PriceUpdatePort, PriceDeletePort {

    private final PriceJpaRepository priceJpaRepository;
    private final PackageJpaRepository packageJpaRepository;
    private final OptionJpaRepository optionJpaRepository;
    private final PriceReferenceImageJpaRepository priceReferenceImageJpaRepository;

    private final JPAQueryFactory queryFactory;

    private final PricePersistenceMapper pricePersistenceMapper;
    private final PackagePersistenceMapper packagePersistenceMapper;
    private final OptionPersistenceMapper optionPersistenceMapper;
    private final PriceReferenceImagePersistenceMapper priceReferenceImagePersistenceMapper;

    @Override
    public List<Price> findByExpertNo(String expertNo) {
        // PriceEntity 리스트 조회
        List<PriceEntity> priceEntities = priceJpaRepository.findByExpertNo(expertNo);

        // Price 서브 도메인 조회 및 및 반환 (Package & Option & PriceRefImage)
        return getPriceDomainList(priceEntities);
    }

    @Override
    public List<Price> findByExpertNoAndThemes(String expertNo,
                                               List<PriceThemeType> priceThemeTypes,
                                               List<SnapSubTheme> snapSubThemes) {
        // Querydsl로 조건에 맞는 PriceEntity 목록 조회
        List<PriceEntity> priceEntities = findPriceEntitiesByCond(expertNo, priceThemeTypes, snapSubThemes);

        // Price 도메인 조립 및 반환 (Package & Option & PriceRefImage)
        return getPriceDomainList(priceEntities);
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
    public Price create(Price price, String expertNo) {

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
    public void update(Price price,
                       List<String> deletedPriceRefImageNos, List<String> deletedPackageNos, List<String> deletedOptionNos
    ) {
        PriceEntity priceEntity = priceJpaRepository.findById(price.getPriceNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        priceEntity.updateEntity(price.getPriceThemeType(), price.getSnapSubTheme());

        // PricePreference 추가/수정/삭제
        updatePriceReferenceImageEntities(price, deletedPriceRefImageNos, priceEntity);

        // Package 추가/수정/삭제
        updatePackageEntities(price, deletedPackageNos, priceEntity);

        // Option 추가/수정/삭제
        updateOptionEntities(price, deletedOptionNos, priceEntity);
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


    /**
     * private 메서드
     */

    private List<PriceEntity> findPriceEntitiesByCond(String expertNo, List<PriceThemeType> priceThemeTypes, List<SnapSubTheme> snapSubThemes) {
        BooleanBuilder where = new BooleanBuilder();
        where.and(priceEntity.expertNo.eq(expertNo));

        List<PriceEntity> priceEntities;

        if (priceThemeTypes == null || priceThemeTypes.isEmpty()) {
            // 타입 조건이 없으면 전문가 기준 전체
            priceEntities = queryFactory
                    .selectFrom(priceEntity)
                    .where(where)
                    .fetch();
        } else {
            // types에서 SNAP 분리
            List<PriceThemeType> nonSnap = new ArrayList<>();
            boolean hasSnap = false;
            for (PriceThemeType t : priceThemeTypes) {
                if (t == PriceThemeType.SNAP) hasSnap = true;
                else nonSnap.add(t);
            }

            // OR 그룹: (nonSnap IN) OR (SNAP AND subTheme IN ...)
            BooleanBuilder orGroup = new BooleanBuilder();

            if (!nonSnap.isEmpty()) {
                // 1) types=[BEAUTY], subThemes=빈 → BEAUTY만
                orGroup.or(priceEntity.priceThemeType.in(nonSnap));
            }

            List<SnapSubTheme> subList = (snapSubThemes == null) ? List.of() : snapSubThemes;
            if (hasSnap && !subList.isEmpty()) {
                // 2) types=[SNAP], subThemes=[FAMILY] → SNAP + FAMILY만
                // 3) types=[SNAP, BEAUTY], subThemes=[FAMILY] → BEAUTY 전체 OR (SNAP+FAMILY)
                // 4)  types=[SNAP], subThemes=[FAMILY, PROFILE] → priceThemeType = SNAP AND snapSubTheme IN (FAMILY, PROFILE)
                orGroup.or(
                        priceEntity.priceThemeType.eq(PriceThemeType.SNAP)
                                .and(priceEntity.snapSubTheme.in(subList))
                );
            }

            priceEntities = queryFactory
                    .selectFrom(priceEntity)
                    .where(where.and(orGroup))
                    .fetch();
        }
        return priceEntities;
    }

    private List<Price> getPriceDomainList(List<PriceEntity> priceEntities) {
        List<Price> prices = new ArrayList<>();

        for (PriceEntity priceEntity : priceEntities) {
            List<Package> packages = findPackagesByPriceNo(priceEntity); // 해당 Price의 Package 불러오기
            List<Option> options = findOptionsByPriceNo(priceEntity); // 해당 Price의 Option 불러오기
            List<PriceReferenceImage> referenceImages = findPriceRefImagesByPriceNo(priceEntity); // 해당 Price의 RefImage 불러오기

            prices.add(pricePersistenceMapper.toDomain(priceEntity, packages, options, referenceImages));
        }
        return prices;
    }

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
        return priceReferenceImageJpaRepository.findAllByPriceEntity_PriceNoOrderByImageOrder(priceEntity.getPriceNo()).stream()
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
        List<PriceReferenceImageEntity> priceReferenceImageEntities = referenceImages.stream()
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
            if (packageNo != null) {
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
        if (!deletedPriceRefImageNos.isEmpty()) {
            priceReferenceImageJpaRepository.deleteByPriceReferenceImageNoIn(deletedPriceRefImageNos);

            // 삭제 후 이미지 순서 재정렬
            List<PriceReferenceImageEntity> images =
                    priceReferenceImageJpaRepository.findAllByPriceEntity_PriceNoOrderByImageOrder(priceEntity.getPriceNo());

            for (int i = 0; i < images.size(); i++) {
                images.get(i).assignImageOrder(i + 1);
            }
        }

        // 추가/수정
        priceReferenceImageJpaRepository.shiftAllImageOrdersToNegative(priceEntity.getPriceNo()); // 추가/수정 전에 모든 imageOrder 값을 임시값으로 변경

        List<PriceReferenceImage> priceReferenceImages = price.getPriceReferenceImages();
        for (PriceReferenceImage domain : priceReferenceImages) {
            String priceRefImageNo = domain.getPriceRefImageNo();
            if (priceRefImageNo != null) {
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
            if (optionNo != null) {
                // PK가 있다 = 수정
                OptionEntity entity = optionJpaRepository.findById(optionNo)
                        .orElseThrow(() -> new RestApiException(_NOT_FOUND));
                entity.updateEntity(domain.getName(), domain.getUnitSize(), domain.getPricePerUnit(), domain.getContents());
            } else {
                // PK가 없다 = 저장
                OptionEntity entity = optionPersistenceMapper.toEntity(domain);
                entity.assignPriceEntity(priceEntity);
                optionJpaRepository.save(entity);
            }
        }
    }
}
