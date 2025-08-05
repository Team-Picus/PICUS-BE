package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.request.SearchPostRequest;
import com.picus.core.post.adapter.in.web.data.response.SearchPostResponse;
import com.picus.core.post.application.port.in.command.SearchPostCommand;
import com.picus.core.post.application.port.in.result.SearchPostResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchPostWebMapper {

    public SearchPostCommand toCommand(SearchPostRequest request) {
        return SearchPostCommand.builder()
                .themeTypes(request.getThemeTypes())
                .snapSubThemes(request.getSnapSubThemes())
                .spaceType(request.getSpaceType())
                .address(request.getAddress())
                .moodTypes(request.getMoodTypes())
                .sortBy(request.getSortBy())
                .sortDirection(request.getSortDirection())
                .cursor(request.getCursor())
                .size(request.getSize())
                .build();
    }


    public SearchPostResponse toResponse(SearchPostResult result) {
        return SearchPostResponse.builder()
                .posts(toPostResponse(result.posts()))
                .isLast(result.isLast())
                .build();
    }

    private List<SearchPostResponse.PostResponse> toPostResponse(List<SearchPostResult.PostResult> posts) {
        return posts.stream()
                .map(postResult -> SearchPostResponse.PostResponse.builder()
                        .postNo(postResult.postNo())
                        .thumbnailUrl(postResult.thumbnailUrl())
                        .authorNickname(postResult.authorNickname())
                        .title(postResult.title())
                        .build()).toList();
    }


}
