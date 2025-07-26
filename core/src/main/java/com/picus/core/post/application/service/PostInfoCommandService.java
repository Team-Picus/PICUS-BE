package com.picus.core.post.application.service;

import com.picus.core.expert.application.port.out.ExpertQueryPort;
import com.picus.core.expert.application.port.out.ExpertCommandPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.post.application.port.in.PostInfoCommand;
import com.picus.core.post.application.port.in.mapper.WritePostAppMapper;
import com.picus.core.post.application.port.in.request.UpdatePostAppReq;
import com.picus.core.post.application.port.in.request.WritePostAppReq;
import com.picus.core.post.application.port.out.PostCommandPort;
import com.picus.core.post.application.port.out.PostQueryPort;
import com.picus.core.post.domain.model.Post;
import com.picus.core.post.domain.model.PostImage;
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
public class PostInfoCommandService implements PostInfoCommand {

    private final UserQueryPort userQueryPort;
    private final ExpertQueryPort expertQueryPort;
    private final ExpertCommandPort expertCommandPort;
    private final PostCommandPort postCommandPort;
    private final PostQueryPort postQueryPort;

    private final WritePostAppMapper appMapper;

    @Override
    public void write(WritePostAppReq writePostAppReq) {

        // 글 작성한 사용자의 expertNo 조회
        String expertNo = getCurrentExpertNo(writePostAppReq.currentUserNo());

        // Post 도메인 조립
        Post post = appMapper.toDomain(writePostAppReq, expertNo);

        // 데이터베이스에 저장
        postCommandPort.save(post);

        // TODO: 저장 후 해당 이미지 키들 레디스에서 삭제
    }

    @Override
    public void update(UpdatePostAppReq updatePostAppReq) {

        // PostImage 순서 중복 검증
        checkPostImageOrder(updatePostAppReq.postImages());

        // 글 작성한 사용자의 expertNo 조회
        String expertNo = getCurrentExpertNo(updatePostAppReq.currentUserNo());

        // Post 조회
        Post post = postQueryPort.findById(updatePostAppReq.postNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // 조회한 Post의 작성자와 현재 expertNo가 같은지 검증
        throwIfNotOwner(expertNo, post.getAuthorNo());

        // Post 수정
        updatePost(post, updatePostAppReq);

        // PostImage 수정
        List<String> deletedPostImageNos = new ArrayList<>();
        updatePostImage(post, updatePostAppReq.postImages(), deletedPostImageNos);

        // 수정사항 데이터베이스 반영
        postCommandPort.update(post, deletedPostImageNos);

        // Expert의 activityAt 최신화
        Expert expert = expertQueryPort.findById(expertNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
        expert.updateLastActivityAt(LocalDateTime.now());
        expertCommandPort.updateExpert(expert);

        // TODO: 저장 후 해당 이미지 키들 레디스에서 삭제
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
