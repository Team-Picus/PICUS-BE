package com.picus.core.post.application.service;

import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.application.port.out.PostUpdatePort;
import com.picus.core.post.domain.Post;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UpdateGalleryServiceTest {

    @Mock
    private UserQueryPort userQueryPort;
    @Mock
    private PostReadPort postReadPort;
    @Mock
    private PostUpdatePort postUpdatePort;

    @InjectMocks
    private UpdateGalleryService updateGalleryService;

    @Test
    @DisplayName("특정 Post를 고정처리하고 기존 고정처리된 Post는 해제된다.")
    public void updateGallery() throws Exception {
        // given

        // 파라미터 정의
        String postNo = "post-123";
        String currentUserNo = "user-123";

        // stubbing
        User user = mock(User.class);
        given(userQueryPort.findById(currentUserNo)).willReturn(user);

        String expertNo = "expert-123";
        given(user.getExpertNo()).willReturn(expertNo);

        Post prevPinnedPost = mock(Post.class);
        given(postReadPort.findByExpertNoAndIsPinnedTrue(expertNo)).willReturn(Optional.of(prevPinnedPost));

        Post curPinnedPost = mock(Post.class);
        given(postReadPort.findById(postNo)).willReturn(Optional.of(curPinnedPost));


        // when - 서비스 호출
        updateGalleryService.update(postNo, currentUserNo);

        // then - 메서드 호출 검증
        then(userQueryPort).should().findById(currentUserNo);
        then(user).should().getExpertNo();
        then(postReadPort).should().findByExpertNoAndIsPinnedTrue(expertNo);
        then(prevPinnedPost).should().unpin();
        then(postUpdatePort).should().update(prevPinnedPost);
        then(postReadPort).should().findById(postNo);
        then(curPinnedPost).should().pin();
        then(postUpdatePort).should().update(curPinnedPost);
    }

}