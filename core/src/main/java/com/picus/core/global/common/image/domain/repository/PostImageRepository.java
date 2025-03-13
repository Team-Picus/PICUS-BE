package com.picus.core.global.common.image.domain.repository;

import com.picus.core.domain.post.domain.entity.image.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
