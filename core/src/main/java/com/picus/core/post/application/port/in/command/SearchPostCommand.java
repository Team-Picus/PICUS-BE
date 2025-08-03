package com.picus.core.post.application.port.in.command;


import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class SearchPostCommand {
    @Builder.Default
    private List<PostThemeType> themeTypes = new ArrayList<>();
    @Builder.Default
    private List<SnapSubTheme> snapSubThemes = new ArrayList<>();
    private SpaceType spaceType;
    private String address;
    @Builder.Default
    private List<PostMoodType> moodTypes = new ArrayList<>();

    @Builder.Default
    private String sortBy = "updatedAt";
    @Builder.Default
    private String sortDirection = "ASC";
    private String lastPostNo;
    @Builder.Default
    private int size = 10;

}
