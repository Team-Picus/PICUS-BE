package com.picus.core.post.application.port.in;

import com.picus.core.post.application.port.in.response.SuggestPostsAppResp;

import java.util.List;

/**
 * 게시물 검색어 추천 유스케이스
 */
public interface SuggestPostsUseCase {
    List<SuggestPostsAppResp> suggest(String keyword, int size);
}
