package com.picus.core.post.application.service;

import com.picus.core.expert.application.port.out.ExpertReadPort;
import com.picus.core.expert.application.port.out.ExpertUpdatePort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.post.application.port.in.mapper.CreatePostCommandMapper;
import com.picus.core.post.application.port.in.command.CreatePostCommand;
import com.picus.core.post.application.port.out.PostCreatePort;
import com.picus.core.post.domain.Post;
import com.picus.core.user.application.port.out.UserReadPort;
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
    UserReadPort userReadPort;
    @Mock
    PostCreatePort postCreatePort;
    @Mock
    CreatePostCommandMapper createPostCommandMapper;
    @Mock
    ExpertReadPort expertReadPort;
    @Mock
    ExpertUpdatePort expertUpdatePort;

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
        given(userReadPort.findById(req.currentUserNo())).willReturn(user);
        String expertNo = "expert_no";
        given(user.getExpertNo()).willReturn(expertNo);
        Post post = mock(Post.class);
        given(createPostCommandMapper.toDomain(req, expertNo)).willReturn(post);
        Expert expert = mock(Expert.class);
        given(expertReadPort.findById(expertNo)).willReturn(Optional.ofNullable(expert));

        // when
        createPostService.create(req);

        // then
        InOrder inOrder = Mockito.inOrder(
                userReadPort, user, createPostCommandMapper, postCreatePort,
                expertReadPort, expert, expert, expertUpdatePort
        );
        then(userReadPort).should(inOrder).findById(req.currentUserNo());
        then(user).should(inOrder).getExpertNo();
        then(createPostCommandMapper).should(inOrder).toDomain(req, expertNo);
        then(postCreatePort).should(inOrder).save(post);
        then(expertReadPort).should(inOrder).findById(expertNo);
        then(expert).should(inOrder).increaseActivityCount();
        then(expert).should(inOrder).updateLastActivityAt(any(LocalDateTime.class));
        then(expertUpdatePort).should(inOrder).update(expert);
    }

}