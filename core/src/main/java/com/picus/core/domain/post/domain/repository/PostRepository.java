package com.picus.core.domain.post.domain.repository;

import com.picus.core.domain.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select distinct p " +
            "from Post p " +
            "left join fetch p.basicOption bo " +
            "left join fetch bo.additionalOptions " +
            "left join fetch p.postCategories " +
            "left join fetch p.postDistricts " +
            "where p.id = :postId")
    Optional<Post> findPostWithDetailsById(@Param("postId") Long postId);
}
