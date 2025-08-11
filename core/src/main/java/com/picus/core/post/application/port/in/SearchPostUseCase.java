package com.picus.core.post.application.port.in;

import com.picus.core.post.application.port.in.command.SearchPostCommand;
import com.picus.core.post.application.port.in.result.SearchPostResult;


public interface SearchPostUseCase {

    /**
     * 게시글을 검색
     *
     * @param command 검색 조건 (테마, 무드, 주소, 정렬 등)
     * @return 검색된 게시글 목록
     */
    SearchPostResult search(SearchPostCommand command);
}
