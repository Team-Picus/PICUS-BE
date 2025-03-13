package com.picus.core.global.common.image.domain.repository;

import com.picus.core.global.common.image.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
