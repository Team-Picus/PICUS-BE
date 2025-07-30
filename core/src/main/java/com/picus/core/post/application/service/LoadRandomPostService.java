package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.LoadRandomPostUseCase;
import com.picus.core.post.application.port.in.result.LoadRandomPostResult;
import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.PostImage;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.shared.exception.code.status.GlobalErrorStatus;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._INTERNAL_SERVER_ERROR;

@UseCase
@RequiredArgsConstructor
public class LoadRandomPostService implements LoadRandomPostUseCase {

    private final PostReadPort postReadPort;
    private final UserReadPort userReadPort;


    @Override
    public List<LoadRandomPostResult> load(int size) {
        // 랜덤으로 게시물 N개 가져오기
        List<Post> posts = postReadPort.findRandomTopN(size);

        // Results 반환
        return posts.stream()
                .map(post -> {
                    // 각 게시물 작성자의 User 정보 조회 (닉네임 정보를 얻기 위해)
                    User user = userReadPort.findByExpertNo(post.getAuthorNo());

                    // 썸네일 뽑기(imageOrder가 1인 PostImage)
                    PostImage thumbnail = post.getPostImages().stream()
                            .filter(postImage -> postImage.getImageOrder() == 1)
                            .findAny()
                            .orElseThrow(() -> new RestApiException(_INTERNAL_SERVER_ERROR));

                    return LoadRandomPostResult.builder()
                            .postNo(post.getPostNo())
                            .nickname(user.getNickname())
                            .thumbnailUrl("") // TODO: file key -> url
                            .build();
                })
                .toList();
    }
}
