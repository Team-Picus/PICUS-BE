package com.picus.core.price.adapter.out.persistence.repository;

import com.picus.core.price.adapter.out.persistence.entity.OptionEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionJpaRepository extends JpaRepository<OptionEntity, String> {
    List<OptionEntity> findByPriceEntity(PriceEntity priceEntity);
}
