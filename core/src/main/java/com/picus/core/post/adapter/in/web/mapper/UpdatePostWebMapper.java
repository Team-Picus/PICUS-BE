package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.request.CreatePostRequest;
import com.picus.core.post.adapter.in.web.data.request.UpdatePostRequest;
import com.picus.core.post.adapter.in.web.data.request.UpdatePostRequest.PostImageRequest;
import com.picus.core.post.application.port.in.command.CreatePostCommand;
import com.picus.core.post.application.port.in.command.UpdatePostCommand;
import com.picus.core.post.application.port.in.command.UpdatePostCommand.UpdatePostImageCommand;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdatePostWebMapper {

    public UpdatePostCommand toCommand(UpdatePostRequest webReq, String postNo, String currentUserNo) {
        return UpdatePostCommand.builder()
                .postNo(postNo)
                .postImages(toPostImageCommands(webReq.postImages()))
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

    private List<UpdatePostImageCommand> toPostImageCommands(List<PostImageRequest> postImageRequests) {
        return postImageRequests.stream()
                .map(webReq ->
                        UpdatePostImageCommand.builder()
                                .postImageNo(webReq.postImageNo())
                                .fileKey(webReq.fileKey())
                                .imageOrder(webReq.imageOrder())
                                .changeStatus(webReq.changeStatus())
                                .build()
                ).toList();
    }
    private List<UpdatePostCommand.PackageCommand> toPackageCommend(List<UpdatePostRequest.PackageRequest> packageRequests) {
        return packageRequests.stream()
                .map(packageRequest ->
                        UpdatePostCommand.PackageCommand.builder()
                                .packageNo(packageRequest.packageNo())
                                .packageThemeType(packageRequest.packageThemeType())
                                .snapSubTheme(packageRequest.snapSubTheme())
                                .build()
                ).toList();
    }
}
