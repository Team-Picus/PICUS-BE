package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.request.WritePostWebReq;
import com.picus.core.post.application.port.in.request.WritePostAppReq;
import com.picus.core.post.domain.model.PostImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WritePostWebMapper {

    public WritePostAppReq toAppReq(WritePostWebReq webReq, String currentUserNo) {
        return WritePostAppReq.builder()
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

    private List<PostImage> toPostImages(WritePostWebReq webReq) {
        return webReq.postImages().stream()
                .map(postImageWebReq ->
                        PostImage.builder()
                                .fileKey(postImageWebReq.fileKey())
                                .imageOrder(postImageWebReq.imageOrder())
                                .build()
                ).toList();
    }
}
