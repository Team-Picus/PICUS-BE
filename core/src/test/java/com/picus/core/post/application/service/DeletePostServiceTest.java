package com.picus.core.post.application.service;

import com.picus.core.expert.application.port.out.ReadExpertPort;
import com.picus.core.expert.application.port.out.UpdateExpertPort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.post.application.port.out.DeletePostPort;
import com.picus.core.post.application.port.out.ReadPostPort;
import com.picus.core.post.domain.Post;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserQueryPort;
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
    UserQueryPort userQueryPort;
    @Mock
    ReadExpertPort readExpertPort;
    @Mock
    UpdateExpertPort updateExpertPort;
    @Mock
    DeletePostPort deletePostPort;
    @Mock
    ReadPostPort readPostPort;

    @InjectMocks
    DeletePostService deletePostService;


    @Test
    @DisplayName("게시물을 삭제한다.")
    public void delete_success() throws Exception {
        // given
        String postNo = "post-123";
        String currentUserNo = "user-123";

        // stubbing
        User user = mock(User.class);
        given(userQueryPort.findById(currentUserNo)).willReturn(user);
        String expertNo = "expert-123";
        given(user.getExpertNo()).willReturn(expertNo);
        Post post = mock(Post.class);
        given(readPostPort.findById(postNo)).willReturn(Optional.of(post));
        given(post.getAuthorNo()).willReturn(expertNo);
        Expert expert = mock(Expert.class);
        given(readExpertPort.findById(expertNo)).willReturn(Optional.of(expert));
        LocalDateTime lastPostAt = LocalDateTime.MAX;
        given(readPostPort.findTopUpdatedAtByExpertNo(post.getAuthorNo())).willReturn(Optional.of(lastPostAt));


        // when
        deletePostService.delete(postNo, currentUserNo);

        // then
        then(userQueryPort).should().findById(currentUserNo);
        then(user).should().getExpertNo();
        then(readPostPort).should().findById(postNo);
        then(deletePostPort).should().delete(postNo);
        then(readExpertPort).should().findById(expertNo);
        then(expert).should().decreaseActivityCount();
        then(readPostPort).should().findTopUpdatedAtByExpertNo(post.getAuthorNo());
        then(expert).should().updateLastActivityAt(lastPostAt);
        then(updateExpertPort).should().update(expert);
    }

    @Test
    @DisplayName("게시물을 삭제할 때 현재 사용자의 expertNo와 Post의 authorNo가 일치하지 않으면 에러 발생")
    public void delete_if_expertNo_NotEqual() throws Exception {
        // given
        String postNo = "post-123";
        String currentUserNo = "user-123";

        // stubbing
        User user = mock(User.class);
        given(userQueryPort.findById(currentUserNo)).willReturn(user);
        String expertNo = "expert-123";
        given(user.getExpertNo()).willReturn(expertNo);
        Post post = mock(Post.class);
        given(readPostPort.findById(postNo)).willReturn(Optional.of(post));
        given(post.getAuthorNo()).willReturn("expert-345");

        // when // then
        assertThatThrownBy(() -> deletePostService.delete(postNo, currentUserNo))
                .isInstanceOf(RestApiException.class);
    }

}