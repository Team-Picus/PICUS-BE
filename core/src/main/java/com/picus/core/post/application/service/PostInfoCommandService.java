package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.PostInfoCommand;
import com.picus.core.post.application.port.in.mapper.WritePostAppMapper;
import com.picus.core.post.application.port.in.request.WritePostAppReq;
import com.picus.core.post.application.port.out.PostCommandPort;
import com.picus.core.post.domain.model.Post;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.out.UserQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional
public class PostInfoCommandService implements PostInfoCommand {

    private final UserQueryPort userQueryPort;
    private final PostCommandPort postCommandPort;

    private final WritePostAppMapper appMapper;

    @Override
    public void writePost(WritePostAppReq writePostAppReq) {

        // 글 작성한 사용자의 expertNo 찾기
        String expertNo = userQueryPort.findById(writePostAppReq.currentUserNo())
                .getExpertNo();

        // Post 도메인 조립
        Post post = appMapper.toDomain(writePostAppReq, expertNo);

        // 데이터베이스에 저장
        postCommandPort.save(post);

        // TODO: 저장 후 해당 이미지 키들 레디스에서 삭제
    }
}
