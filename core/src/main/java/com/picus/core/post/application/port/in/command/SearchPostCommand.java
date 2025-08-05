package com.picus.core.post.application.port.in.command;

import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import lombok.Builder;

import java.util.List;

@Builder
public record SearchPostCommand(
        List<PostThemeType> themeTypes,
        List<SnapSubTheme> snapSubThemes,
        SpaceType spaceType,
        String address,
        List<PostMoodType> moodTypes,
        String sortBy,
        String sortDirection,
        String cursor,
        int size
) {}