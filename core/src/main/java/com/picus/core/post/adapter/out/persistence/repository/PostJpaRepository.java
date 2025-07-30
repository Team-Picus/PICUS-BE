package com.picus.core.post.adapter.out.persistence.repository;

import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<PostEntity, String> {

    @Query("SELECT p.updatedAt FROM PostEntity p WHERE p.expertNo = :expertNo ORDER BY p.updatedAt DESC limit 1")
    Optional<LocalDateTime> findTopUpdatedAtByExpertNo(String expertNo);

    Optional<PostEntity> findByExpertNoAndIsPinnedTrue(String expertNo);

    @Query("""
                SELECT p FROM PostEntity p 
                WHERE p.title LIKE CONCAT(:keyword, '%') 
                   OR p.title LIKE CONCAT('%', :keyword, '%') 
                ORDER BY 
                    CASE 
                        WHEN p.title LIKE CONCAT(:keyword, '%') THEN 0 
                        ELSE 1 
                    END,
                    p.title ASC
                LIMIT :size
            """)
    List<PostEntity> findTopNByTitleContainingOrderByTitle(String keyword, int size);

    @Query(value = "select * from posts ORDER BY RAND() LIMIT :size", nativeQuery = true)
    List<PostEntity> findRandomTopN(int size);
}
