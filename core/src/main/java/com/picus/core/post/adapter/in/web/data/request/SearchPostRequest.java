package com.picus.core.post.adapter.in.web.data.request;

import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SearchPostRequest {

    private final List<PostThemeType> themeTypes;
    private final List<SnapSubTheme> snapSubThemes;
    private final SpaceType spaceType;
    private final String address;
    private final List<PostMoodType> moodTypes;
    private final String sortBy;
    private final String sortDirection;
    private final String lastPostNo;
    private final int size;

    public SearchPostRequest(
            List<PostThemeType> themeTypes,
            List<SnapSubTheme> snapSubThemes,
            SpaceType spaceType,
            String address,
            List<PostMoodType> moodTypes,
            String sortBy,
            String sortDirection,
            String lastPostNo,
            Integer size
    ) {
        this.themeTypes = themeTypes != null ? themeTypes : new ArrayList<>();
        this.snapSubThemes = snapSubThemes != null ? snapSubThemes : new ArrayList<>();
        this.spaceType = spaceType;
        this.address = address;
        this.moodTypes = moodTypes != null ? moodTypes : new ArrayList<>();
        this.sortBy = sortBy != null ? sortBy : "updatedAt";
        this.sortDirection = sortDirection != null ? sortDirection : "DESC";
        this.lastPostNo = lastPostNo;
        this.size = size != null && size > 0 ? size : 10;
    }

}