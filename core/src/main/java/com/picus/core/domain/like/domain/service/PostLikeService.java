package com.picus.core.domain.like.domain.service;

import com.picus.core.domain.like.domain.entity.PostLike;
import com.picus.core.domain.like.domain.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;

    public PostLike save(Long userNo, Long postNo) {
        return postLikeRepository.save(new PostLike(userNo, postNo));
    }

    public void delete(Long userNo, Long postNo) {
        postLikeRepository.unlike(userNo, postNo);
    }
}
