package com.picus.core.post.application.service;

import com.picus.core.expert.application.port.out.ExpertReadPort;
import com.picus.core.expert.application.port.out.ExpertUpdatePort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.post.application.port.out.PostDeletePort;
import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.domain.Post;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class DeletePostServiceTest {


    @Mock
    ExpertReadPort expertReadPort;
    @Mock
    ExpertUpdatePort expertUpdatePort;
    @Mock
    PostDeletePort postDeletePort;
    @Mock
    PostReadPort postReadPort;

    @InjectMocks
    DeletePostService deletePostService;


    @Test
    @DisplayName("게시물을 삭제한다.")
    public void delete_success() throws Exception {
        // given
        String postNo = "post-123";
        String currentUserNo = "user-123";

        // stubbing
        Post mockPost = mock(Post.class);
        given(postReadPort.findById(postNo)).willReturn(Optional.of(mockPost));
        given(mockPost.getAuthorNo()).willReturn(currentUserNo);

        Expert mockExpert = mock(Expert.class);
        given(expertReadPort.findById(currentUserNo)).willReturn(Optional.of(mockExpert));
        LocalDateTime lastPostAt = LocalDateTime.MAX;
        given(postReadPort.findTopUpdatedAtByExpertNo(mockPost.getAuthorNo())).willReturn(Optional.of(lastPostAt));


        // when
        deletePostService.delete(postNo, currentUserNo);

        // then
        then(postReadPort).should().findById(postNo);
        then(postDeletePort).should().delete(postNo);
        then(expertReadPort).should().findById(currentUserNo);
        then(mockExpert).should().decreaseActivityCount();
        then(postReadPort).should().findTopUpdatedAtByExpertNo(mockPost.getAuthorNo());
        then(mockExpert).should().updateLastActivityAt(lastPostAt);
        then(expertUpdatePort).should().update(mockExpert);
    }

    @Test
    @DisplayName("게시물을 삭제할 때 현재 사용자의 expertNo와 Post의 authorNo가 일치하지 않으면 에러 발생")
    public void delete_if_expertNo_NotEqual() throws Exception {
        // given
        String postNo = "post-123";
        String currentUserNo = "user-123";

        // stubbing
        Post post = mock(Post.class);
        given(postReadPort.findById(postNo)).willReturn(Optional.of(post));
        given(post.getAuthorNo()).willReturn("user-345");

        // when // then
        assertThatThrownBy(() -> deletePostService.delete(postNo, currentUserNo))
                .isInstanceOf(RestApiException.class);
    }

}