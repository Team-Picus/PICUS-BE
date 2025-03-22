package com.picus.core.domain.shared.image.domain.repository;

import com.picus.core.domain.shared.image.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
