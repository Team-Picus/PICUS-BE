package com.picus.core.old.domain.shared.image.application.usecase;

import com.picus.core.old.domain.post.domain.entity.image.PostImageResource;
import com.picus.core.old.domain.shared.image.application.dto.response.ImageUrl;
import com.picus.core.old.domain.shared.image.domain.repository.PostImageResourceCustomRepository;
import com.picus.core.old.domain.shared.image.domain.service.PostImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageUseCase {
    private final PostImageService postImageService;
    private final PostImageResourceCustomRepository postImageResourceCustomRepository;

    public List<ImageUrl> findPostImages(Long postId) {
        return postImageService.findImages(postId)
                .stream()
                .map(postImageResource -> new ImageUrl(postImageResource.getPreSignedKey()))
                .toList();
    }

    public Map<Long, ImageUrl> getOldestImageResourceByPostIds(List<Long> postIds) {
        List<PostImageResource> resources = postImageResourceCustomRepository.findByPostNoInOrderByUploadedAtAsc(postIds);

        // 각 postNo별로 가장 오래된(정렬상 첫번째) 요소의 preSignedKey를 ImageUrl로 매핑
        Map<Long, ImageUrl> oldestResourceMap = resources.stream()
                .collect(Collectors.groupingBy(
                        PostImageResource::getPostNo,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> new ImageUrl(list.get(0).getPreSignedKey())
                        )
                ));
        return oldestResourceMap;
    }

}
