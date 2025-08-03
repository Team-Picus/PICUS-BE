package com.picus.core.post.application.service;

import com.picus.core.post.adapter.out.persistence.dto.SearchPostCond;
import com.picus.core.post.application.port.in.SearchPostUseCase;
import com.picus.core.post.application.port.in.command.SearchPostCommand;
import com.picus.core.post.application.port.in.mapper.SearchPostCommandMapper;
import com.picus.core.post.application.port.in.result.SearchPostResult;
import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.domain.Post;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@UseCase
@RequiredArgsConstructor
public class SearchPostService implements SearchPostUseCase {

    private final PostReadPort postReadPort;
    private final UserReadPort userReadPort;

    private final SearchPostCommandMapper commandMapper;


    @Override
    public SearchPostResult search(SearchPostCommand command) {

        // 게시물 조회
        SearchPostCond searchPostCond = commandMapper.toCond(command);

        List<Post> searchResults = postReadPort.findBySearchCond(
                searchPostCond,
                command.lastPostNo(),
                command.sortBy(),
                command.sortDirection(),
                command.size() + 1 // 마지막 페이지인지 확인을 위해 1개 더 조회
        );

        // 리턴 값 조립
        List<SearchPostResult.PostResult> postResults = new ArrayList<>();
        for (Post post : searchResults) {
            // 각 게시물의 작성자 닉네임 조회
            User user = userReadPort.findByExpertNo(post.getAuthorNo());

            postResults.add(SearchPostResult.PostResult.builder()
                    .postNo(post.getPostNo())
                    .authorNickname(user.getNickname())
                    .thumbnailUrl("") // TODO: file key -> url 변환 로직
                    .title(post.getTitle())
                    .build());
        }

        return SearchPostResult.builder()
                .posts(postResults)
                .isLast(searchResults.size() <= command.size()) // 검색 결과 수가 요청 수보다 작거나 같으면 더이상 남은 데이터가 없다는 의미
                .build();
    }
}
