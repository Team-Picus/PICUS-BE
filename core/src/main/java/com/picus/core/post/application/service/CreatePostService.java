package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.CreatePostUseCase;
import com.picus.core.post.application.port.in.mapper.WritePostAppMapper;
import com.picus.core.post.application.port.in.request.CreatePostAppReq;
import com.picus.core.post.application.port.out.CreatePostPort;
import com.picus.core.post.domain.Post;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.*;

@UseCase
@RequiredArgsConstructor
@Transactional
public class CreatePostService implements CreatePostUseCase {

    private final UserQueryPort userQueryPort;
    private final CreatePostPort createPostPort;

    private final WritePostAppMapper appMapper;

    @Override
    public void create(CreatePostAppReq createPostAppReq) {

        // 글 작성한 사용자의 expertNo 조회
        String expertNo = getCurrentExpertNo(createPostAppReq.currentUserNo());

        // Post 도메인 조립
        Post post = appMapper.toDomain(createPostAppReq, expertNo);

        // 데이터베이스에 저장
        createPostPort.save(post);

        // TODO: 저장 후 해당 이미지 키들 레디스에서 삭제

        // TODO Expert의 활동 수, 최근 활동일 갱신
    }
    /**
     * private 메서드
     */
    private String getCurrentExpertNo(String userNo) {
        User user = userQueryPort.findById(userNo);
        return Optional.ofNullable(user.getExpertNo())
                .orElseThrow(() -> new RestApiException(_FORBIDDEN));
    }
}
