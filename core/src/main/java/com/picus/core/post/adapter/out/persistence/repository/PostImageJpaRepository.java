package com.picus.core.post.adapter.out.persistence.repository;

import com.picus.core.post.adapter.out.persistence.entity.PostImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostImageJpaRepository extends JpaRepository<PostImageEntity, String> {
    List<PostImageEntity> findByPostEntity_PostNo(String postNo);

    void deleteByPostImageNoIn(List<String> deletedPostImageNos);

    List<PostImageEntity> findAllByPostEntity_PostNoOrderByImageOrder(String postNo);

    @Modifying(clearAutomatically = true) // clearAutomatically = true: update 후 영속성 컨텍스트 자동 초기화 (flush 반영 보장)
    @Query(value = "UPDATE post_images SET image_order = image_order * -1 WHERE post_no = :postNo", nativeQuery = true)
    void shiftAllImageOrdersToNegative(String postNo);
}
