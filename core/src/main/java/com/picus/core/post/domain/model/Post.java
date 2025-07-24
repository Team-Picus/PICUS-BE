package com.picus.core.post.domain.model;

import com.picus.core.post.domain.model.vo.PostMoodType;
import com.picus.core.post.domain.model.vo.PostStatus;
import com.picus.core.post.domain.model.vo.PostThemeType;
import com.picus.core.post.domain.model.vo.SpaceType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class Post {

    private String postNo;

    private String authorNo;
    private String packageNo;

    private String title;
    private String oneLineDescription;
    private String detailedDescription;
    private List<PostThemeType> postThemeTypes;
    private List<PostMoodType> postMoodTypes;
    private SpaceType spaceType;
    private String spaceAddress;
    private Boolean isPinned;
    private List<PostImage> postImages;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
