package com.picus.core.post.application.port.in.result;

import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record LoadPostDetailResult(
        String postNo,
        List<PostImageResult> images,
        String title,
        String oneLineDescription,
        AuthorInfo authorInfo,
        String detailedDescription,
        List<PostThemeType> themeTypes,
        List<SnapSubTheme> snapSubThemes,
        List<PostMoodType> moodTypes,
        SpaceType spaceType,
        String spaceAddress,
        List<String> packageNos,
        LocalDateTime updatedAt
) {
    @Builder
    public record PostImageResult(
            String imageNo,
            String fileKey,
            String imageUrl,
            Integer imageOrder
    ) {
    }

    @Builder
    public record AuthorInfo(
            String expertNo,
            String nickname
    ) {}
}
