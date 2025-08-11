package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.LoadCommentByPostUseCase;
import com.picus.core.post.application.port.in.result.LoadCommentByPostResult;
import com.picus.core.post.application.port.out.CommentReadPort;
import com.picus.core.post.domain.Comment;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@UseCase
@RequiredArgsConstructor
public class LoadCommentByPostService implements LoadCommentByPostUseCase {

    private final CommentReadPort commentReadPort;

    private final UserReadPort userReadPort;

    @Override
    public List<LoadCommentByPostResult> load(String postNo) {

        // 특정 Post의 Comment 조회
        List<Comment> comments = commentReadPort.findByPostNo(postNo);

        List<LoadCommentByPostResult> results = new ArrayList<>();
        for (Comment comment : comments) {
            // 각 Comment의 작성자 정보 조회 (닉네임 + 프로필이미지)
            User user = userReadPort.findById(comment.getAuthorNo());

            // Result 객체 조립
            LoadCommentByPostResult result = LoadCommentByPostResult.builder()
                    .commentNo(comment.getCommentNo())
                    .authorNo(comment.getAuthorNo())
                    .authorNickname(user.getNickname())
                    .authorProfileImageUrl("") // TODO: file key -> url 변환 로직
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .build();
            results.add(result);
        }
        return results;
    }
}
