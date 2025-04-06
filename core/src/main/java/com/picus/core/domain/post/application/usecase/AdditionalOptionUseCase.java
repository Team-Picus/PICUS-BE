package com.picus.core.domain.post.application.usecase;


import com.picus.core.domain.post.application.dto.request.AdditionalOptionCreate;
import com.picus.core.domain.post.application.dto.request.AdditionalOptionUpdate;
import com.picus.core.domain.post.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdditionalOptionUseCase {
    private final PostService postService;


    @Transactional
    public boolean removeAdditionalOption(Long postId, Long additionalOptionId) {
        return postService.removeAdditionalOption(postId, additionalOptionId);
    }

    @Transactional
    public boolean updateAdditionalOption(Long postId, AdditionalOptionUpdate additionalOptionUpdate) {
        return postService.updateAdditionalOption(postId,
                additionalOptionUpdate.additionalOptionCreate(),
                additionalOptionUpdate.id());
    }

    @Transactional
    public boolean addAdditionalOption(Long postId, AdditionalOptionCreate additionalOptionCreate) {
        return postService.addAdditionalOption(postId, additionalOptionCreate);
    }
}
