package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.response.LoadPostDetailResponse;
import com.picus.core.post.application.port.in.result.LoadPostDetailResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoadPostDetailWebMapper {

    public LoadPostDetailResponse toResponse(LoadPostDetailResult result) {

        return LoadPostDetailResponse.builder()
                .postNo(result.postNo())
                .title(result.title())
                .oneLineDescription(result.oneLineDescription())
                .detailedDescription(result.detailedDescription())
                .themeTypes(result.themeTypes())
                .snapSubThemes(result.snapSubThemes())
                .moodTypes(result.moodTypes())
                .spaceType(result.spaceType())
                .spaceAddress(result.spaceAddress())
                .packageNo(result.packageNo())
                .updatedAt(result.updatedAt())
                .authorInfo(toAuthorInfo(result.authorInfo()))
                .images(toPostImageResponse(result.images()))
                .build();
    }

    private LoadPostDetailResponse.AuthorInfo toAuthorInfo(LoadPostDetailResult.AuthorInfo authorInfo) {
        return LoadPostDetailResponse.AuthorInfo.builder()
                .expertNo(authorInfo.expertNo())
                .nickname(authorInfo.nickname())
                .build();
    }

    private List<LoadPostDetailResponse.PostImageResponse> toPostImageResponse(List<LoadPostDetailResult.PostImageResult> images) {
        return images.stream()
                .map(image -> LoadPostDetailResponse.PostImageResponse.builder()
                        .imageNo(image.imageNo())
                        .fileKey(image.fileKey())
                        .imageUrl(image.imageUrl())
                        .imageOrder(image.imageOrder())
                        .build()
                ).toList();
    }
}
