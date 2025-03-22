package com.picus.core.domain.shared.image.domain.service;

import com.picus.core.domain.post.domain.entity.image.PostImageResource;
import com.picus.core.domain.shared.image.domain.repository.PostImageResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostImageService {

    private final PostImageResourceRepository postImageResourceRepository;

    public List<PostImageResource> findImages(Long postId) {
        return postImageResourceRepository.findByPostNo(postId);
    }
}
