package com.picus.core.post.application.port.in.request;

import com.picus.core.post.domain.model.PostImage;
import com.picus.core.post.domain.model.vo.PostMoodType;
import com.picus.core.post.domain.model.vo.PostThemeType;
import com.picus.core.post.domain.model.vo.SpaceType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public record WritePostAppReq(
        List<PostImage> postImages,
        String title,
        String oneLineDescription,
        String detailedDescription,
        List<PostThemeType> postThemeTypes,
        List<PostMoodType> postMoodTypes,
        SpaceType spaceType,
        String spaceAddress,
        String packageNo,
        String currentUserNo
) {
}
