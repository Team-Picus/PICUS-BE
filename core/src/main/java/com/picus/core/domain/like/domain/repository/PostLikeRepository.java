package com.picus.core.domain.like.domain.repository;

import com.picus.core.domain.like.domain.entity.post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    @Modifying
    @Transactional
    @Query("delete from PostLike pl where pl.userNo = :userNo and pl.postNo = :postNo")
    void delete(@Param("userNo") Long userNo, @Param("postNo") Long postNo);
}
