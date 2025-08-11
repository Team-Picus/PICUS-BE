package com.picus.core.post.application.port.in.command;

import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdatePostCommand(
        String postNo,
        List<UpdatePostImageCommand> postImages,
        String title,
        String oneLineDescription,
        String detailedDescription,
        List<PostThemeType> postThemeTypes,
        List<SnapSubTheme> snapSubThemes,
        List<PostMoodType> postMoodTypes,
        SpaceType spaceType,
        String spaceAddress,
        String packageNo,
        String currentUserNo
) {

    @Builder
    public record UpdatePostImageCommand(
            String postImageNo,
            String fileKey,
            Integer imageOrder,
            ChangeStatus changeStatus
    ) {}
}
