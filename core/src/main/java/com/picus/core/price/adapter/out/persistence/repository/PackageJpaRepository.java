package com.picus.core.price.adapter.out.persistence.repository;

import com.picus.core.price.adapter.out.persistence.entity.PackageEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageJpaRepository extends JpaRepository<PackageEntity, String> {
    List<PackageEntity> findByPriceEntity(PriceEntity priceEntity);
}
