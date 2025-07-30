package com.picus.core.post.application.service;

import com.picus.core.expert.application.port.out.ReadExpertPort;
import com.picus.core.expert.application.port.out.UpdateExpertPort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.post.application.port.in.mapper.CreatePostAppMapper;
import com.picus.core.post.application.port.in.request.CreatePostCommand;
import com.picus.core.post.application.port.out.CreatePostPort;
import com.picus.core.post.domain.Post;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CreatePostServiceTest {

    @Mock
    UserQueryPort userQueryPort;
    @Mock
    CreatePostPort createPostPort;
    @Mock
    CreatePostAppMapper createPostAppMapper;
    @Mock
    ReadExpertPort readExpertPort;
    @Mock
    UpdateExpertPort updateExpertPort;

    @InjectMocks
    CreatePostService createPostService;

    @Test
    @DisplayName("게시물을 작성한다.")
    public void create_success() throws Exception {
        // given
        CreatePostCommand req = CreatePostCommand.builder()
                .currentUserNo("user-456")
                .build();

        // Stubbing
        User user = mock(User.class);
        given(userQueryPort.findById(req.currentUserNo())).willReturn(user);
        String expertNo = "expert_no";
        given(user.getExpertNo()).willReturn(expertNo);
        Post post = mock(Post.class);
        given(createPostAppMapper.toDomain(req, expertNo)).willReturn(post);
        Expert expert = mock(Expert.class);
        given(readExpertPort.findById(expertNo)).willReturn(Optional.ofNullable(expert));

        // when
        createPostService.create(req);

        // then
        InOrder inOrder = Mockito.inOrder(
                userQueryPort, user, createPostAppMapper, createPostPort,
                readExpertPort, expert, expert, updateExpertPort
        );
        then(userQueryPort).should(inOrder).findById(req.currentUserNo());
        then(user).should(inOrder).getExpertNo();
        then(createPostAppMapper).should(inOrder).toDomain(req, expertNo);
        then(createPostPort).should(inOrder).save(post);
        then(readExpertPort).should(inOrder).findById(expertNo);
        then(expert).should(inOrder).increaseActivityCount();
        then(expert).should(inOrder).updateLastActivityAt(any(LocalDateTime.class));
        then(updateExpertPort).should(inOrder).update(expert);
    }

}