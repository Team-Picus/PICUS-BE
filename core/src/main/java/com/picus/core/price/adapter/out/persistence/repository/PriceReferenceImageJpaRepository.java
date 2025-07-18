package com.picus.core.price.adapter.out.persistence.repository;

import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceReferenceImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceReferenceImageJpaRepository extends JpaRepository<PriceReferenceImageEntity, String> {
    List<PriceReferenceImageEntity> findByPriceEntity(PriceEntity priceEntity);
}
