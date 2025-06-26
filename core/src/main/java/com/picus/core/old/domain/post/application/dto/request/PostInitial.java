package com.picus.core.old.domain.post.application.dto.request;

import com.picus.core.old.domain.shared.area.District;
import com.picus.core.old.domain.shared.category.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;


public record PostInitial(
        @NotNull Long postId,
        @NotBlank String title,
        @NotBlank String detail,
        @NotBlank Set<District> availableAreas,
        @NotNull Integer basicPrice,
//        @NotBlank List<String> imageKeys,
        @NotBlank Set<Category> categories,
        List<AdditionalOptionCreate> additionalOptions
) {}