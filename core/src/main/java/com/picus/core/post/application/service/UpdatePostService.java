package com.picus.core.post.application.service;

import com.picus.core.expert.application.port.out.ReadExpertPort;
import com.picus.core.expert.application.port.out.UpdateExpertPort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.post.application.port.in.UpdatePostUseCase;
import com.picus.core.post.application.port.in.request.UpdatePostAppReq;
import com.picus.core.post.application.port.out.ReadPostPort;
import com.picus.core.post.application.port.out.UpdatePostPort;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.PostImage;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.picus.core.post.application.port.in.request.UpdatePostAppReq.*;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.*;

@UseCase
@RequiredArgsConstructor
@Transactional
public class UpdatePostService implements UpdatePostUseCase {

    private final UserQueryPort userQueryPort;
    private final ReadExpertPort readExpertPort;
    private final UpdateExpertPort updateExpertPort;
    private final UpdatePostPort updatePostPort;
    private final ReadPostPort readPostPort;

    @Override
    public void update(UpdatePostAppReq updatePostAppReq) {

        // PostImage 순서 중복 검증
        checkPostImageOrder(updatePostAppReq.postImages());

        // 글 작성한 사용자의 expertNo 조회
        String expertNo = getCurrentExpertNo(updatePostAppReq.currentUserNo());

        // Post 조회
        Post post = readPostPort.findById(updatePostAppReq.postNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // 수정할 Post의 작성자와 현재 expertNo가 같은지 검증
        throwIfNotOwner(expertNo, post.getAuthorNo());

        // Post 수정
        updatePost(post, updatePostAppReq);

        // PostImage 수정
        List<String> deletedPostImageNos = new ArrayList<>();
        updatePostImage(post, updatePostAppReq.postImages(), deletedPostImageNos);

        // 수정사항 데이터베이스 반영
        updatePostPort.updateWithPostImage(post, deletedPostImageNos);

        // Expert의 activityAt 최신화
        Expert expert = readExpertPort.findById(expertNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
        expert.updateLastActivityAt(LocalDateTime.now());
        updateExpertPort.update(expert);

        // TODO: 새로 저장된 이미지 키들 레디스에서 삭제
        // TODO: 삭제된 이미지들 S3에서 삭제
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

    private void checkPostImageOrder(List<UpdatePostImageAppReq> updatePostImageAppReqs) {
        Set<Integer> imageOrderSet = new HashSet<>();
        for (UpdatePostImageAppReq imageAppReq : updatePostImageAppReqs) {
            if (!imageOrderSet.add(imageAppReq.imageOrder())) {
                throw new RestApiException(_BAD_REQUEST);
            }
        }
    }

    private void updatePostImage(Post post, List<UpdatePostImageAppReq> imageAppReqs, List<String> deletedPostImageNos) {
        for (UpdatePostImageAppReq imageAppReq : imageAppReqs) {
            switch (imageAppReq.changeStatus()) {
                case NEW:
                    post.addPostImage(PostImage.builder()
                            .fileKey(imageAppReq.fileKey())
                            .imageOrder(imageAppReq.imageOrder())
                            .build());
                    break;
                case UPDATE:
                    post.updatePostImage(PostImage.builder()
                            .postImageNo(imageAppReq.postImageNo())
                            .fileKey(imageAppReq.fileKey())
                            .imageOrder(imageAppReq.imageOrder())
                            .build());
                    break;
                case DELETE:
                    post.deletePostImage(imageAppReq.postImageNo());
                    deletedPostImageNos.add(imageAppReq.postImageNo());
                    break;
            }
        }
    }

    private void updatePost(Post post, UpdatePostAppReq updatePostAppReq) {
        post.updatePost(
                updatePostAppReq.title(),
                updatePostAppReq.oneLineDescription(),
                updatePostAppReq.detailedDescription(),
                updatePostAppReq.postThemeTypes(),
                updatePostAppReq.postMoodTypes(),
                updatePostAppReq.spaceType(),
                updatePostAppReq.spaceAddress(),
                updatePostAppReq.packageNo()
        );
    }
}
