package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.request.CreatePostRequest;
import com.picus.core.post.application.port.in.command.CreatePostCommand;
import com.picus.core.post.domain.PostImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreatePostWebMapper {

    public CreatePostCommand toCommand(CreatePostRequest webReq, String currentUserNo) {
        return CreatePostCommand.builder()
                .postImages(toPostImages(webReq))
                .title(webReq.title())
                .oneLineDescription(webReq.oneLineDescription())
                .detailedDescription(webReq.detailedDescription())
                .postThemeTypes(webReq.postThemeTypes())
                .snapSubThemes(webReq.snapSubThemes())
                .postMoodTypes(webReq.postMoodTypes())
                .spaceType(webReq.spaceType())
                .spaceAddress(webReq.spaceAddress())
                .packageNo(webReq.packageNo())
                .authorNo(currentUserNo)
                .build();
    }

    private List<PostImage> toPostImages(CreatePostRequest webReq) {
        return webReq.postImages().stream()
                .map(postImageWebReq ->
                        PostImage.builder()
                                .fileKey(postImageWebReq.fileKey())
                                .imageOrder(postImageWebReq.imageOrder())
                                .build()
                ).toList();
    }
}
