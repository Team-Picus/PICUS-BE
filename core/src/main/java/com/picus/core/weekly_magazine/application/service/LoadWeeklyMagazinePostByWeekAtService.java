package com.picus.core.weekly_magazine.application.service;

import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.domain.Post;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import com.picus.core.weekly_magazine.application.port.in.LoadWeeklyMagazinePostByWeekAtUseCase;
import com.picus.core.weekly_magazine.application.port.in.result.LoadWeeklyMagazinePostByWeekAtResult;
import com.picus.core.weekly_magazine.application.port.out.WeeklyMagazineReadPort;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazinePost;
import com.picus.core.weekly_magazine.domain.model.vo.WeekAt;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@UseCase
@RequiredArgsConstructor
public class LoadWeeklyMagazinePostByWeekAtService implements LoadWeeklyMagazinePostByWeekAtUseCase {

    private final WeeklyMagazineReadPort weeklyMagazineReadPort;

    private final PostReadPort postReadPort;
    private final UserReadPort userReadPort;

    @Override
    public List<LoadWeeklyMagazinePostByWeekAtResult> load(WeekAt weekAt) {

        // 특정 주차의 주간 매거진 게시물 조회
        List<WeeklyMagazinePost> weeklyMagazinePosts =
                weeklyMagazineReadPort.findWeeklyMagazinePostByWeekAt(weekAt.getYear(), weekAt.getMonth(), weekAt.getWeek());

        // 주간 매거진 게시물의 상세정보 조회(title 등)
        List<String> postNoList = weeklyMagazinePosts.stream()
                .map(WeeklyMagazinePost::getPostNo)
                .toList();

        List<Post> posts = postReadPort.findByIdList(postNoList); // TODO: 구현 필요

        // Results 조립
        List<LoadWeeklyMagazinePostByWeekAtResult> results = new ArrayList<>();
        for (Post post : posts) {
            // 각 게시물의 작성자 정보 조회(닉네임)
            User user = userReadPort.findById(post.getAuthorNo());
            results.add(
                    LoadWeeklyMagazinePostByWeekAtResult.builder()
                    .postNo(post.getPostNo())
                    .authorNickname(user.getNickname())
                    .postTitle(post.getTitle())
                    .thumbnailUrl("") // TODO: file key -> url 변환 로직
                    .build()
            );
        }

        return results;
    }
}
