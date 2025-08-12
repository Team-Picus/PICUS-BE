package com.picus.core.post.application.port.in.command;

import com.picus.core.post.domain.PostImage;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.SpaceType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record CreatePostCommand(
        List<PostImage> postImages,
        String title,
        String oneLineDescription,
        String detailedDescription,
        List<PostMoodType> postMoodTypes,
        SpaceType spaceType,
        String spaceAddress,
        List<PackageCommand> packages,
        String currentUserNo
) {

    @Builder
    public record PackageCommand(
            @NotNull String packageNo,
            @NotNull String packageThemeType,
            String snapSubTheme
    ){}
}
