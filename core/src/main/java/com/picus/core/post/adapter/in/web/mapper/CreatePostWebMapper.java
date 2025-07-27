package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.request.CreatePostWebReq;
import com.picus.core.post.application.port.in.request.CreatePostAppReq;
import com.picus.core.post.domain.PostImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreatePostWebMapper {

    public CreatePostAppReq toAppReq(CreatePostWebReq webReq, String currentUserNo) {
        return CreatePostAppReq.builder()
                .postImages(toPostImages(webReq))
                .title(webReq.title())
                .oneLineDescription(webReq.oneLineDescription())
                .detailedDescription(webReq.detailedDescription())
                .postThemeTypes(webReq.postThemeTypes())
                .postMoodTypes(webReq.postMoodTypes())
                .spaceType(webReq.spaceType())
                .spaceAddress(webReq.spaceAddress())
                .packageNo(webReq.packageNo())
                .currentUserNo(currentUserNo)
                .build();
    }

    private List<PostImage> toPostImages(CreatePostWebReq webReq) {
        return webReq.postImages().stream()
                .map(postImageWebReq ->
                        PostImage.builder()
                                .fileKey(postImageWebReq.fileKey())
                                .imageOrder(postImageWebReq.imageOrder())
                                .build()
                ).toList();
    }
}
