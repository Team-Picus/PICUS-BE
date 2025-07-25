package com.picus.core.user.adapter.out.persistence.repository;

import com.picus.core.user.adapter.out.persistence.entity.TermEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TermJpaRepository extends JpaRepository<TermEntity, String> {

    @Query("SELECT t FROM TermEntity t WHERE t.deletedAt IS NULL")
    List<TermEntity> findAll();
}
