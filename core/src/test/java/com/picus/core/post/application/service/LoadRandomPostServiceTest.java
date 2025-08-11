package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.result.LoadRandomPostResult;
import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.PostImage;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class LoadRandomPostServiceTest {

    @Mock
    private PostReadPort postReadPort;
    @Mock
    private UserReadPort userReadPort;

    @InjectMocks
    private LoadRandomPostService loadRandomPostService;


    @Test
    @DisplayName("랜덤으로 게시물 N개 가져온다.")
    public void load() throws Exception {
        // given
        int size = 5;

        List<Post> posts = List.of(
                createPost("post-1", "au-1"),
                createPost("post-2", "au-2"),
                createPost("post-3", "au-3"),
                createPost("post-4", "au-4"),
                createPost("post-5", "au-5")
        );
        given(postReadPort.findRandomTopNByIsPinnedTrue(size)).willReturn(posts);

        List<User> users = List.of(
                createUser("nick1"),
                createUser("nick2"),
                createUser("nick3"),
                createUser("nick4"),
                createUser("nick5")
        );
        for (int i = 0; i < posts.size(); i++) {
            given(userReadPort.findByExpertNo(posts.get(i).getAuthorNo()))
                    .willReturn(users.get(i));
        }

        // when
        List<LoadRandomPostResult> results = loadRandomPostService.load(size);

        // then - 리턴값 검증
        assertThat(results).hasSize(5)
                .extracting(
                        LoadRandomPostResult::postNo,
                        LoadRandomPostResult::nickname,
                        LoadRandomPostResult::thumbnailUrl
                ).containsExactlyInAnyOrder(
                        tuple("post-1", "nick1", ""), // TODO: file key -> url 변환 필요
                        tuple("post-2", "nick2", ""),
                        tuple("post-3", "nick3", ""),
                        tuple("post-4", "nick4", ""),
                        tuple("post-5", "nick5", "")
                );
        // then - 메서드 호출 검증
        then(postReadPort).should().findRandomTopNByIsPinnedTrue(size);
        for (Post post : posts) {
            then(userReadPort).should().findByExpertNo(post.getAuthorNo());
        }
    }

    private Post createPost(String postNo, String authorNo) {
        return Post.builder()
                .postNo(postNo)
                .authorNo(authorNo)
                .postImages(List.of(createPostImage()))
                .build();
    }

    private PostImage createPostImage() {
        return PostImage.builder()
                .imageOrder(1)
                .build();
    }

    private User createUser(String nickname) {
        return User.builder()
                .nickname(nickname)
                .build();
    }
}