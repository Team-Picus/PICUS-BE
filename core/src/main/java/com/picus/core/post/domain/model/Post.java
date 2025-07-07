package com.picus.core.post.domain.model;

import com.picus.core.post.domain.model.vo.PostMoodType;
import com.picus.core.post.domain.model.vo.PostThemeType;
import com.picus.core.post.domain.model.vo.SpaceType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post {

    private String postNo;

    private String authorNo;
    private String packageNo;

    private String title;
    private String oneLineDescription;
    private String detailedDescription;
    private List<PostThemeType> postThemeTypes = new ArrayList<>();
    private List<PostMoodType> postMoodTypes = new ArrayList<>();
    private SpaceType spaceType;
    private String spaceAddress;
    private List<PostImage> postImages;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
