package com.picus.core.post.application.service;

import com.picus.core.expert.application.port.out.ExpertReadPort;
import com.picus.core.expert.application.port.out.ExpertUpdatePort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.post.application.port.in.CreatePostUseCase;
import com.picus.core.post.application.port.in.mapper.CreatePostCommandMapper;
import com.picus.core.post.application.port.in.command.CreatePostCommand;
import com.picus.core.post.application.port.out.PostCreatePort;
import com.picus.core.post.domain.Post;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.*;

@UseCase
@RequiredArgsConstructor
@Transactional
public class CreatePostService implements CreatePostUseCase {

    private final PostCreatePort postCreatePort;

    private final ExpertReadPort expertReadPort;
    private final ExpertUpdatePort expertUpdatePort;

    private final CreatePostCommandMapper appMapper;

    @Override
    public void create(CreatePostCommand createPostCommand) {

        String expertNo = createPostCommand.currentUserNo(); // User와 Expert는 pk가 같음

        // Post 도메인 조립
        Post post = appMapper.toDomain(createPostCommand);

        // 데이터베이스에 저장
        postCreatePort.save(post);

        // TODO: 저장 후 해당 이미지 키들 레디스에서 삭제

        // Expert의 활동 수, 최근 활동일 갱신
        updateExpertInfo(expertNo);
    }
    /**
     * private 메서드
     */

    // TODO: 낙관적 락 처리 (재시도처리) 필요
    private void updateExpertInfo(String expertNo) {
        Expert expert = expertReadPort.findById(expertNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // 해당 Expert의 활동수 1 증가 시키기
        expert.increaseActivityCount();

        // 해당 Expert의 최근 활동일 갱신하기
        expert.updateLastActivityAt(LocalDateTime.now());

        // 데이터베이스에 수정사항 반영
        expertUpdatePort.update(expert);
    }
}
