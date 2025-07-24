package com.picus.core.post.application.port.request;

import com.picus.core.post.domain.model.vo.PostMoodType;
import com.picus.core.post.domain.model.vo.PostThemeType;
import com.picus.core.post.domain.model.vo.SpaceType;

import java.util.List;

public record WritePostAppReq(
        List<String> imageKeys,
        String title,
        String oneLineDescription,
        String detailedDescription,
        List<PostThemeType> postThemeTypes,
        List<PostMoodType> postMoodTypes,
        SpaceType spaceType,
        String spaceAddress,
        String packageNo
) {
}
