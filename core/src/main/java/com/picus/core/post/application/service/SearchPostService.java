package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.SearchPostUseCase;
import com.picus.core.post.application.port.in.command.SearchPostCommand;
import com.picus.core.post.application.port.in.result.SearchPostResult;
import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.shared.annotation.UseCase;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class SearchPostService implements SearchPostUseCase {

    private final PostReadPort postReadPort;




    @Override
    public SearchPostResult search(SearchPostCommand command) {

        // 게시물 조회

        // 각 게시물의 작성자 닉네임 조회


        return null;
    }
}
