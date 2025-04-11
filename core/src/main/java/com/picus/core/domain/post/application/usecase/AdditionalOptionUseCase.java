package com.picus.core.domain.post.application.usecase;


import com.picus.core.domain.post.application.dto.request.AdditionalOptionCreate;
import com.picus.core.domain.post.application.dto.request.AdditionalOptionUpdate;
import com.picus.core.domain.post.application.dto.response.AdditionalOptionResponse;
import com.picus.core.domain.post.domain.entity.Post;
import com.picus.core.domain.post.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<AdditionalOptionResponse> getAdditionalOptions(Long postId) {
        return postService.findPostByIdWithAllDetails(postId)
                .getBasicOption()
                .getAdditionalOptions()
                .stream()
                .map(additionalOption -> new AdditionalOptionResponse(
                        additionalOption.getId(),
                        additionalOption.getName(),
                        additionalOption.getPricePerUnit(),
                        additionalOption.getMax(),
                        additionalOption.getBase(),
                        additionalOption.getIncrement()
                ))
                .toList();
    }
}
