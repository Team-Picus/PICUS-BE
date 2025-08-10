package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.LoadPostDetailUseCase;
import com.picus.core.post.application.port.in.result.LoadPostDetailResult;
import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.domain.Post;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadPostDetailService implements LoadPostDetailUseCase {

    private final PostReadPort postReadPort;

    private final UserReadPort userReadPort;

    @Override
    public LoadPostDetailResult load(String postNo) {
        // postNo로 Post 조회
        Post post = postReadPort.findById(postNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // 해당 post의 작성자 닉네임 조회
        User user = userReadPort.findByExpertNo(post.getAuthorNo());
        String nickname = user.getNickname();

        // Result 조합
        return createResult(post, nickname);
    }

    private LoadPostDetailResult createResult(Post post, String nickname) {
        return LoadPostDetailResult.builder()
                .postNo(post.getPostNo())
                .images(createPostImageResult(post))
                .title(post.getTitle())
                .oneLineDescription(post.getOneLineDescription())
                .detailedDescription(post.getDetailedDescription())
                .themeTypes(post.getPostThemeTypes())
                .snapSubThemes(post.getSnapSubThemes())
                .moodTypes(post.getPostMoodTypes())
                .spaceType(post.getSpaceType())
                .spaceAddress(post.getSpaceAddress())
                .packageNos(post.getPackageNos())
                .updatedAt(post.getUpdatedAt())
                .authorInfo(createAuthorInfo(post.getAuthorNo(), nickname))
                .build();
    }

    private List<LoadPostDetailResult.PostImageResult> createPostImageResult(Post post) {
        return post.getPostImages().stream()
                .map(image -> LoadPostDetailResult.PostImageResult.builder()
                        .imageNo(image.getPostImageNo())
                        .fileKey(image.getFileKey())
                        .imageUrl("") // TODO: file key -> url
                        .imageOrder(image.getImageOrder())
                        .build())
                .toList();
    }

    private LoadPostDetailResult.AuthorInfo createAuthorInfo(String authorNo, String nickname) {
        return LoadPostDetailResult.AuthorInfo
                .builder()
                .expertNo(authorNo)
                .nickname(nickname)
                .build();
    }
}
