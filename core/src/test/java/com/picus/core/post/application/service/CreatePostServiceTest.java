package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.mapper.WritePostAppMapper;
import com.picus.core.post.application.port.in.request.CreatePostAppReq;
import com.picus.core.post.application.port.out.PostCommandPort;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CreatePostServiceTest {

    @Mock
    UserQueryPort userQueryPort;
    @Mock
    PostCommandPort postCommandPort;
    @Mock
    WritePostAppMapper writePostAppMapper;

    @InjectMocks
    CreatePostService createPostService;

    @Test
    @DisplayName("게시물을 작성한다.")
    public void create_success() throws Exception {
        // given
        CreatePostAppReq req = CreatePostAppReq.builder()
                .currentUserNo("user-456")
                .build();

        User user = Mockito.mock(User.class);
        given(userQueryPort.findById(req.currentUserNo())).willReturn(user);
        String expertNo = "expert_no";
        given(user.getExpertNo()).willReturn(expertNo);
        Post post = Mockito.mock(Post.class);
        given(writePostAppMapper.toDomain(req, expertNo)).willReturn(post);

        // when
        createPostService.create(req);

        // then
        InOrder inOrder = Mockito.inOrder(
                userQueryPort, user, writePostAppMapper, postCommandPort
        );
        then(userQueryPort).should(inOrder).findById(req.currentUserNo());
        then(user).should(inOrder).getExpertNo();
        then(writePostAppMapper).should(inOrder).toDomain(req, expertNo);
        then(postCommandPort).should(inOrder).save(post);
    }

}