package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.request.UpdatePostRequest;
import com.picus.core.post.adapter.in.web.data.request.UpdatePostRequest.PostImageRequest;
import com.picus.core.post.application.port.in.command.UpdatePostCommand;
import com.picus.core.post.application.port.in.command.UpdatePostCommand.UpdatePostImageAppReq;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdatePostWebMapper {

    public UpdatePostCommand toAppReq(UpdatePostRequest webReq, String postNo, String currentUserNo) {
        return UpdatePostCommand.builder()
                .postNo(postNo)
                .postImages(toPostImages(webReq.postImages()))
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

    private List<UpdatePostImageAppReq> toPostImages(List<PostImageRequest> postImageRequests) {
        return postImageRequests.stream()
                .map(webReq ->
                        UpdatePostImageAppReq.builder()
                                .postImageNo(webReq.postImageNo())
                                .fileKey(webReq.fileKey())
                                .imageOrder(webReq.imageOrder())
                                .changeStatus(webReq.changeStatus())
                                .build()
                ).toList();
    }
}
