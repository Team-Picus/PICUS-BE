package com.picus.core.post.application.service;

import com.picus.core.expert.application.port.out.ExpertReadPort;
import com.picus.core.expert.application.port.out.ExpertUpdatePort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.post.application.port.in.UpdatePostUseCase;
import com.picus.core.post.application.port.in.command.UpdatePostCommand;
import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.application.port.out.PostUpdatePort;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.PostImage;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.shared.exception.code.status.GlobalErrorStatus;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.picus.core.post.application.port.in.command.ChangeStatus.DELETE;
import static com.picus.core.post.application.port.in.command.UpdatePostCommand.*;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.*;

@UseCase
@RequiredArgsConstructor
@Transactional
public class UpdatePostService implements UpdatePostUseCase {

    private final ExpertReadPort expertReadPort;
    private final ExpertUpdatePort expertUpdatePort;
    private final PostUpdatePort postUpdatePort;
    private final PostReadPort postReadPort;

    @Override
    public void update(UpdatePostCommand updatePostCommand) {

        // PostImage 순서 중복 검증
        checkPostImageOrder(updatePostCommand.postImages());

        // 글 작성한 사용자의 expertNo 조회
        String expertNo = updatePostCommand.currentUserNo(); // User와 Expert는 pk를 공유

        // Post 조회
        Post post = postReadPort.findById(updatePostCommand.postNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // 수정할 Post의 작성자와 현재 expertNo가 같은지 검증
        throwIfNotOwner(expertNo, post.getAuthorNo());

        // Post 수정
        updatePost(post, updatePostCommand);

        // PostImage 수정
        List<String> deletedPostImageNos = new ArrayList<>();
        updatePostImage(post, updatePostCommand.postImages(), deletedPostImageNos);

        // 수정사항 데이터베이스 반영
        postUpdatePort.updateWithPostImage(post, deletedPostImageNos);

        // TODO: 낙관적 락 처리 (재시도처리) 필요
        // Expert의 activityAt 최신화
        Expert expert = expertReadPort.findById(expertNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
        expert.updateLastActivityAt(LocalDateTime.now());
        expertUpdatePort.update(expert);

        // TODO: 새로 저장된 이미지 키들 레디스에서 삭제
        // TODO: 삭제된 이미지들 S3에서 삭제
    }

    /**
     * private 메서드
     */

    private void throwIfNotOwner(String expertNo, String priceExpertNo) {
        if (!expertNo.equals(priceExpertNo))
            throw new RestApiException(_FORBIDDEN);
    }

    private void checkPostImageOrder(List<UpdatePostImageCommand> updatePostImageCommands) {
        Set<Integer> imageOrderSet = new HashSet<>();
        for (UpdatePostImageCommand command : updatePostImageCommands) {
            if (!command.changeStatus().equals(DELETE) && !imageOrderSet.add(command.imageOrder())) {
                throw new RestApiException(_BAD_REQUEST);
            }
        }
    }

    private void updatePostImage(Post post, List<UpdatePostImageCommand> imageCommands, List<String> deletedPostImageNos) {
        for (UpdatePostImageCommand imageCommand : imageCommands) {
            switch (imageCommand.changeStatus()) {
                case NEW:
                    post.addPostImage(PostImage.builder()
                            .fileKey(imageCommand.fileKey())
                            .imageOrder(imageCommand.imageOrder())
                            .build());
                    break;
                case UPDATE:
                    post.updatePostImage(PostImage.builder()
                            .postImageNo(imageCommand.postImageNo())
                            .fileKey(imageCommand.fileKey())
                            .imageOrder(imageCommand.imageOrder())
                            .build());
                    break;
                case DELETE:
                    post.deletePostImage(imageCommand.postImageNo());
                    deletedPostImageNos.add(imageCommand.postImageNo());
                    break;
            }
        }
    }

    private void updatePost(Post post, UpdatePostCommand updatePostCommand) {
        List<String> packageNos = new ArrayList<>();
        Set<PostThemeType> postThemeTypes = new HashSet<>(); // 중복된 테마가 넘어올 수 있으므로 Set
        Set<SnapSubTheme> snapSubThemes = new HashSet<>();
        setPackageInfo(updatePostCommand.packages(), packageNos, postThemeTypes, snapSubThemes);
        post.updatePost(
                updatePostCommand.title(),
                updatePostCommand.oneLineDescription(),
                updatePostCommand.detailedDescription(),
                packageNos,
                postThemeTypes.stream().toList(),
                snapSubThemes.stream().toList(),
                updatePostCommand.postMoodTypes(),
                updatePostCommand.spaceType(),
                updatePostCommand.spaceAddress()
        );
    }

    private void setPackageInfo(List<UpdatePostCommand.PackageCommand> packages, List<String> packageNos,
                                Set<PostThemeType> postThemeTypes, Set<SnapSubTheme> snapSubThemes) {
        for (UpdatePostCommand.PackageCommand packageCommand : packages) {
            packageNos.add(packageCommand.packageNo());
            try {
                postThemeTypes.add(PostThemeType.valueOf(packageCommand.packageThemeType()));
                snapSubThemes.add(SnapSubTheme.valueOf(packageCommand.snapSubTheme()));
            } catch (IllegalArgumentException e) {
                throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
            }
        }
    }
}
