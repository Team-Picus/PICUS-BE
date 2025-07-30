package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.SuggestPostsUseCase;
import com.picus.core.post.application.port.in.mapper.SuggestPostsCommandMapper;
import com.picus.core.post.application.port.in.result.SuggestPostsResult;
import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.domain.Post;
import com.picus.core.shared.annotation.UseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class SuggestPostsService implements SuggestPostsUseCase {

    private final PostReadPort postReadPort;
    private final SuggestPostsCommandMapper appMapper;

    @Override
    public List<SuggestPostsResult> suggest(String keyword, int size) {

        // 해당 키워드가 포함되는 제목을 가진 Post 특정 갯수 조회
        List<Post> posts = postReadPort.findTopNByTitleContainingOrderByTitle(keyword, size);

        // 응답 객체로 변환
        return posts.stream()
                .map(appMapper::toAppResp)
                .toList();
    }
}
