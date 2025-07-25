package com.picus.core.expert.adapter.out.persistence.repository;

import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpertJpaRepository extends JpaRepository<ExpertEntity, String> {

    @Query("select e from ExpertEntity e join fetch e.userEntity eu " +
            "where eu.nickname like concat('%', :keyword, '%') order by eu.nickname")
    List<ExpertEntity> findByNicknameContaining(String keyword);

    @Query("select e from ExpertEntity e join fetch e.userEntity eu " +
            "where eu.nickname like concat('%', :keyword, '%') order by eu.nickname")
    List<ExpertEntity> findByNicknameContainingLimited(String keyword, Pageable pageable);
}
