package com.picus.core.old.domain.shared.image.domain.repository;

import com.picus.core.old.domain.post.domain.entity.image.PostImageResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageResourceCustomRepository extends JpaRepository<PostImageResource, Long> {

    List<PostImageResource> findByPostNoInOrderByUploadedAtAsc(List<Long> postIds);
}
