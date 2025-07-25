package com.picus.core.moodboard.adapter.out.persistence.repository;

import com.picus.core.moodboard.adapter.out.persistence.entity.MoodboardEntity;
import com.picus.core.moodboard.adapter.out.persistence.entity.MoodboardId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoodboardJpaRepository extends JpaRepository<MoodboardEntity, MoodboardId> {
}
