package com.picus.core.post.adapter.out.persistence.repository;

import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<PostEntity, String> {

    @Query("SELECT p.updatedAt FROM PostEntity p WHERE p.expertNo = :expertNo ORDER BY p.updatedAt DESC limit 1")
    Optional<LocalDateTime> findTopUpdatedAtByExpertNo(String expertNo);
}
