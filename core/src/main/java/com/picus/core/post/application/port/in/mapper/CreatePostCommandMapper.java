package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.application.port.in.command.CreatePostCommand;
import com.picus.core.post.domain.Post;
import org.springframework.stereotype.Component;

@Component
public class CreatePostCommandMapper {

    public Post toDomain(CreatePostCommand appReq, String expertNo) {
        return Post.builder()
                .packageNo(appReq.packageNo())
                .authorNo(expertNo)
                .title(appReq.title())
                .oneLineDescription(appReq.oneLineDescription())
                .detailedDescription(appReq.detailedDescription())
                .postThemeTypes(appReq.postThemeTypes())
                .snapSubThemes(appReq.snapSubThemes())
                .postMoodTypes(appReq.postMoodTypes())
                .spaceType(appReq.spaceType())
                .spaceAddress(appReq.spaceAddress())
                .isPinned(false) // 고정여부 초기값은 false
                .postImages(appReq.postImages())
                .build();
    }
}
