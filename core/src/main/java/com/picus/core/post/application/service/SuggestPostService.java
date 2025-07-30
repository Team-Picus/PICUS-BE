package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.SuggestPostUseCase;
import com.picus.core.post.application.port.in.mapper.SuggestPostCommandMapper;
import com.picus.core.post.application.port.in.result.SuggestPostResult;
import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.domain.Post;
import com.picus.core.shared.annotation.UseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class SuggestPostService implements SuggestPostUseCase {

    private final PostReadPort postReadPort;
    private final SuggestPostCommandMapper appMapper;

    @Override
    public List<SuggestPostResult> suggest(String keyword, int size) {

        // 해당 키워드가 포함되는 제목을 가진 Post 특정 갯수 조회
        List<Post> posts = postReadPort.findTopNByTitleContainingOrderByTitle(keyword, size);

        // 응답 객체로 변환
        return posts.stream()
                .map(appMapper::toResult)
                .toList();
    }
}
