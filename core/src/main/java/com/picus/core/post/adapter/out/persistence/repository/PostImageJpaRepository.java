package com.picus.core.post.adapter.out.persistence.repository;

import com.picus.core.post.adapter.out.persistence.entity.PostImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageJpaRepository extends JpaRepository<PostImageEntity, String> {
    List<PostImageEntity> findByPostEntity_PostNo(String postNo);
}
