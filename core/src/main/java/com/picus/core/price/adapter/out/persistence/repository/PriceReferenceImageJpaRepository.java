package com.picus.core.price.adapter.out.persistence.repository;

import com.picus.core.price.adapter.out.persistence.entity.PriceReferenceImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PriceReferenceImageJpaRepository extends JpaRepository<PriceReferenceImageEntity, String> {
    List<PriceReferenceImageEntity> findByPriceEntity_PriceNo(String priceNo);

    List<PriceReferenceImageEntity> findAllByPriceEntity_PriceNoOrderByImageOrder(String priceNo);

    void deleteByPriceEntity_PriceNo(String priceNo);

    void deleteByPriceReferenceImageNoIn(List<String> deletedPriceRefImageNos);

    @Modifying(clearAutomatically = true) // clearAutomatically = true: update 후 영속성 컨텍스트 자동 초기화 (flush 반영 보장)
    @Query(value = "UPDATE price_reference_images SET image_order = image_order * -1 WHERE price_no = :priceNo", nativeQuery = true)
    void shiftAllImageOrdersToNegative(String priceNo);
}
