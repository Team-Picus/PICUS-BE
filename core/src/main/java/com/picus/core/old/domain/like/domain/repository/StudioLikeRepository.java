package com.picus.core.old.domain.like.domain.repository;

import com.picus.core.old.domain.like.domain.entity.studio.StudioLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface StudioLikeRepository extends JpaRepository<StudioLike, Long> {

    @Modifying
    @Transactional
    @Query("delete from StudioLike sl where sl.userNo = :userNo and sl.studioNo = :studioNo")
    void delete(Long userNo, Long studioNo);
}
