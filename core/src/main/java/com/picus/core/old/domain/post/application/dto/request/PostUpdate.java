package com.picus.core.old.domain.post.application.dto.request;

import com.picus.core.old.domain.shared.area.District;
import com.picus.core.old.domain.shared.category.Category;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

public record PostUpdate(
        @NotNull Long postId,
        String title,
        String detail,
        Set<District> availableAreas, // 포함되면 기존의 것을 완전히 대체함
        Set<Category> categories, // 포함되면 기존의 것을 완전히 대체함.
        Integer basicPrice,
        List<AdditionalOptionCreate> additionalOptionsToAdd, // 추가될 옵션
        List<AdditionalOptionUpdate> additionalOptionsToUpdate, // 변경될 옵션
        List<Long> additionalOptionsToRemove // 삭제될 옵션의 id들
) {}