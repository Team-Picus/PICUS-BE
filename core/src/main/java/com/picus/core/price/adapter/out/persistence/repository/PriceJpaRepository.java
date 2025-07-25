package com.picus.core.price.adapter.out.persistence.repository;

import com.amazonaws.services.dynamodbv2.xspec.L;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceJpaRepository extends JpaRepository<PriceEntity, String> {

    List<PriceEntity> findByExpertNo(String expertNo);
}
