package com.picus.core.post.adapter.in.web.data.request;

import com.picus.core.post.application.port.in.command.ChangeStatus;
import com.picus.core.post.domain.vo.PostMoodType;
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
        @NotNull @Size(min = 1) List<PostMoodType> postMoodTypes,
        @NotNull SpaceType spaceType,
        @NotBlank String spaceAddress,
        @Valid @NotNull @Size(min = 1) List<PackageRequest> packages
) {

    @Builder
    public record PostImageRequest(
            String postImageNo,
            @NotBlank String fileKey,
            @NotNull Integer imageOrder,
            @NotNull ChangeStatus changeStatus
    ){}

    @Builder
    public record PackageRequest(
            @NotNull String packageNo,
            @NotNull String packageThemeType,
            String snapSubTheme
    ){}
}
