package com.picus.core.post.application.port.in;

import com.picus.core.post.application.port.in.result.SuggestPostResult;

import java.util.List;

/**
 * 게시물 검색어 추천 유스케이스
 */
public interface SuggestPostUseCase {
    List<SuggestPostResult> suggest(String keyword, int size);
}
