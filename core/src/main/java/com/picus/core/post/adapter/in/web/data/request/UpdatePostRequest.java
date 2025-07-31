package com.picus.core.post.adapter.in.web.data.request;

import com.picus.core.post.application.port.in.command.ChangeStatus;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdatePostRequest(
        @Valid @NotNull @Size(min = 1, max = 10) List<PostImageRequest> postImages,
        @NotBlank String title,
        @NotBlank String oneLineDescription,
        String detailedDescription,
        @NotNull @Size(min = 1) List<PostThemeType> postThemeTypes,
        List<SnapSubTheme> snapSubThemes,
        @NotNull @Size(min = 1) List<PostMoodType> postMoodTypes,
        @NotNull SpaceType spaceType,
        @NotBlank String spaceAddress,
        @NotBlank String packageNo
) {

    @Builder
    public record PostImageRequest(
            String postImageNo,
            String fileKey,
            Integer imageOrder,
            ChangeStatus changeStatus
    ){}
}
