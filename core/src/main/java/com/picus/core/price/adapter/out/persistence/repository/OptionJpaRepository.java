package com.picus.core.price.adapter.out.persistence.repository;

import com.picus.core.price.adapter.out.persistence.entity.OptionEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OptionJpaRepository extends JpaRepository<OptionEntity, String> {
    List<OptionEntity> findByPriceEntity_PriceNo(String priceNo);

    void deleteByPriceEntity_PriceNo(String priceNo);

    void deleteByOptionNoIn(List<String> deletedOptionNos);

    @Query("SELECT o FROM OptionEntity o WHERE o.optionNo IN :optionNos")
    List<OptionEntity> findByIds(@Param("optionNos") List<String> optionNos);
}
