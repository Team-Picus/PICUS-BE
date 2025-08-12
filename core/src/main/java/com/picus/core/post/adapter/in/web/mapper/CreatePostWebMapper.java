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
                .postImages(toPostImages(webReq.postImages()))
                .title(webReq.title())
                .oneLineDescription(webReq.oneLineDescription())
                .detailedDescription(webReq.detailedDescription())
                .postMoodTypes(webReq.postMoodTypes())
                .spaceType(webReq.spaceType())
                .spaceAddress(webReq.spaceAddress())
                .packages(toPackageCommend(webReq.packages()))
                .currentUserNo(currentUserNo)
                .build();
    }

    private List<PostImage> toPostImages(List<CreatePostRequest.PostImageRequest> postImageRequests) {
        return postImageRequests.stream()
                .map(postImageRequest ->
                        PostImage.builder()
                                .fileKey(postImageRequest.fileKey())
                                .imageOrder(postImageRequest.imageOrder())
                                .build()
                ).toList();
    }

    private List<CreatePostCommand.PackageCommand> toPackageCommend(List<CreatePostRequest.PackageRequest> packageRequests) {
        return packageRequests.stream()
                .map(packageRequest ->
                        CreatePostCommand.PackageCommand.builder()
                                .packageNo(packageRequest.packageNo())
                                .packageThemeType(packageRequest.packageThemeType())
                                .snapSubTheme(packageRequest.snapSubTheme())
                                .build()
                ).toList();
    }
}
