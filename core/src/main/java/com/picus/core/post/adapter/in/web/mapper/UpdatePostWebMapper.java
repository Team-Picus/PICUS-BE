package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.request.UpdatePostWebReq;
import com.picus.core.post.adapter.in.web.data.request.UpdatePostWebReq.PostImageWebReq;
import com.picus.core.post.application.port.in.request.UpdatePostCommand;
import com.picus.core.post.application.port.in.request.UpdatePostCommand.UpdatePostImageAppReq;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdatePostWebMapper {

    public UpdatePostCommand toAppReq(UpdatePostWebReq webReq, String postNo, String currentUserNo) {
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

    private List<UpdatePostImageAppReq> toPostImages(List<PostImageWebReq> postImageWebReqs) {
        return postImageWebReqs.stream()
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
