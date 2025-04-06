package com.picus.core.domain.shared.image.domain.repository;

import com.picus.core.domain.post.domain.entity.image.PostImageResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostImageResourceRepository extends JpaRepository<PostImageResource, Long> {

    List<PostImageResource> findByPostNo(Long postNo);
}
