package com.picus.core.post.application.port.in.command;

import com.picus.core.post.domain.PostImage;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SpaceType;
import lombok.Builder;

import java.util.List;

@Builder
public record CreatePostCommand(
        List<PostImage> postImages,
        String title,
        String oneLineDescription,
        String detailedDescription,
        List<PostThemeType> postThemeTypes,
        List<PostMoodType> postMoodTypes,
        SpaceType spaceType,
        String spaceAddress,
        String packageNo,
        String currentUserNo
) {
}
