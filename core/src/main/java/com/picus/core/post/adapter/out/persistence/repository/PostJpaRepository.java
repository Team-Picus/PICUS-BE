package com.picus.core.post.adapter.out.persistence.repository;

import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<PostEntity, String> {
}
