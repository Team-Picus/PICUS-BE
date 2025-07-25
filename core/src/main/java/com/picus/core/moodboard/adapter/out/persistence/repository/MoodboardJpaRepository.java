package com.picus.core.moodboard.adapter.out.persistence.repository;

import com.picus.core.moodboard.adapter.out.persistence.entity.MoodboardEntity;
import com.picus.core.moodboard.adapter.out.persistence.entity.MoodboardId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MoodboardJpaRepository extends JpaRepository<MoodboardEntity, MoodboardId> {

    @Query("select mb from MoodboardEntity mb where mb.user.userNo = :user_no")
    List<MoodboardEntity> findByUser(@Param("user_no") String userNo);
}
