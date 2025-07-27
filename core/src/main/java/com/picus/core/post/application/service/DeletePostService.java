package com.picus.core.post.application.service;

import com.picus.core.expert.application.port.out.ReadExpertPort;
import com.picus.core.expert.application.port.out.UpdateExpertPort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.post.application.port.in.DeletePostUseCase;
import com.picus.core.post.application.port.out.DeletePostPort;
import com.picus.core.post.application.port.out.ReadPostPort;
import com.picus.core.post.domain.Post;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.*;

@UseCase
@RequiredArgsConstructor
@Transactional
public class DeletePostService implements DeletePostUseCase {

    private final UserQueryPort userQueryPort;

    private final ReadExpertPort readExpertPort;
    private final UpdateExpertPort updateExpertPort;

    private final ReadPostPort readPostPort;
    private final DeletePostPort deletePostPort;

    @Override
    public void delete(String postNo, String currentUserNo) {
        // 현재 사용자의 전문가 번호 조회
        String expertNo = getCurrentExpertNo(currentUserNo);

        // 삭제할 Post 조회
        Post post = readPostPort.findById(postNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // 삭제할 Post의 작성자와 현재 사용자의 expertNo가 같은지 검증
        throwIfNotOwner(expertNo, post.getAuthorNo());

        // 삭제
        deletePostPort.delete(postNo);

        // TODO: S3에서 이미지들 삭제

        // Expert의 활동 수, 최근 활동 일 갱신
        updateExpertInfo(expertNo);
    }

    /**
     * private 메서드
     */
    private String getCurrentExpertNo(String userNo) {
        User user = userQueryPort.findById(userNo);
        return Optional.ofNullable(user.getExpertNo())
                .orElseThrow(() -> new RestApiException(_FORBIDDEN));
    }

    private void throwIfNotOwner(String expertNo, String priceExpertNo) {
        if (!expertNo.equals(priceExpertNo))
            throw new RestApiException(_FORBIDDEN);
    }


    private void updateExpertInfo(String expertNo) {
        // 해당 Expert의 ActivityCount 1 감소 시키기
        Expert expert = readExpertPort.findById(expertNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
        expert.decreaseActivityCount();

        // 해당 Expert의 Post, Reservation 중 최근 작성한 것의 날짜를 Expert의 lastActivityAt으로 업데이트

        // 해당 Expert의 Post중 제일 최근에 작성한 글의 날짜 조회
        Optional<LocalDateTime> lastPostAt = readPostPort.findTopUpdatedAtByExpertNo(expertNo);

        // TODO: 해당 Expert의 제일 최근에 생성된 Reservation의 날짜 조회
        Optional<LocalDateTime> lastReservationAt = Optional.empty();

        // 둘 중 더 최근인 날짜를 expert의 lastActivityAt으로 업데이트
        LocalDateTime lastActivityAt = null;

        if (lastPostAt.isPresent() && lastReservationAt.isPresent()) {
            lastActivityAt = lastPostAt.get().isAfter(lastReservationAt.get()) ? lastPostAt.get() : lastReservationAt.get();
        } else if (lastPostAt.isPresent()) {
            lastActivityAt = lastPostAt.get();
        } else if (lastReservationAt.isPresent()) {
            lastActivityAt = lastReservationAt.get();
        }
        expert.updateLastActivityAt(lastActivityAt);

        // 데이터베이스에 수정사항 반영
        updateExpertPort.update(expert);
    }
}
