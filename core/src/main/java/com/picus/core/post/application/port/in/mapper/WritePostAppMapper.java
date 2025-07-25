package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.application.port.in.request.WritePostAppReq;
import com.picus.core.post.domain.model.Post;
import org.springframework.stereotype.Component;

@Component
public class WritePostAppMapper {

    public Post toDomain(WritePostAppReq appReq, String expertNo) {
        return Post.builder()
                .packageNo(appReq.packageNo())
                .authorNo(expertNo)
                .title(appReq.title())
                .oneLineDescription(appReq.oneLineDescription())
                .detailedDescription(appReq.detailedDescription())
                .postThemeTypes(appReq.postThemeTypes())
                .postMoodTypes(appReq.postMoodTypes())
                .spaceType(appReq.spaceType())
                .spaceAddress(appReq.spaceAddress())
                .isPinned(false) // 고정여부 초기값은 false
                .postImages(appReq.postImages())
                .build();
    }
}
