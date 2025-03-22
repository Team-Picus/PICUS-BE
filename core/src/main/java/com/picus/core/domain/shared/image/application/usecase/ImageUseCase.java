package com.picus.core.domain.shared.image.application.usecase;

import com.picus.core.domain.post.domain.entity.image.PostImageResource;
import com.picus.core.domain.shared.image.application.dto.response.ImageUrl;
import com.picus.core.domain.shared.image.domain.service.PostImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageUseCase {
    private final PostImageService postImageService;

    public List<ImageUrl> findPostImages(Long postId) {
        return postImageService.findImages(postId)
                .stream()
                .map(postImageResource -> new ImageUrl(postImageResource.getPreSignedKey()))
                .toList();
    }
}
