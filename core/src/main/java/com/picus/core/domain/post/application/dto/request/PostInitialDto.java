package com.picus.core.domain.post.application.dto.request;

import com.picus.core.domain.shared.area.entity.District;
import com.picus.core.domain.shared.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public record PostInitialDto(
        @NotNull Long postId,
        @NotBlank String title,
        @NotBlank String detail,
        @NotBlank List<District> availableAreas,
        @NotNull Integer basicPrice,
//        @NotBlank List<String> imageKeys,
        @NotBlank List<Category> categories,
        List<AdditionalOptionDto> additionalOptions
) {}