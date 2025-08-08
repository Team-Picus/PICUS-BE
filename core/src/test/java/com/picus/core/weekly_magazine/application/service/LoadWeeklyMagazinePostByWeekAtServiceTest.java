package com.picus.core.weekly_magazine.application.service;

import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.domain.Post;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import com.picus.core.weekly_magazine.application.port.in.result.LoadWeeklyMagazinePostByWeekAtResult;
import com.picus.core.weekly_magazine.application.port.out.WeeklyMagazineReadPort;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazinePost;
import com.picus.core.weekly_magazine.domain.model.vo.WeekAt;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class LoadWeeklyMagazinePostByWeekAtServiceTest {

    @Mock
    private WeeklyMagazineReadPort weeklyMagazineReadPort;
    @Mock
    private PostReadPort postReadPort;
    @Mock
    private UserReadPort userReadPort;

    @InjectMocks
    private LoadWeeklyMagazinePostByWeekAtService service;

    @Test
    @DisplayName("특정 주차의 게시물을 조회한다.")
    public void load() throws Exception {
        // given
        WeekAt weekAt = WeekAt.builder()
                .year(2025)
                .month(6)
                .week(2)
                .build();

        String postNo = "post-123";
        WeeklyMagazinePost mockWeeklyMagazinePost = createMockWeeklyMagazinePost(postNo);
        given(weeklyMagazineReadPort.findWeeklyMagazinePostByWeekAt(
                weekAt.getYear(), weekAt.getMonth(), weekAt.getWeek()))
                .willReturn(List.of(mockWeeklyMagazinePost));

        Post mockPost = createMockPost(postNo);
        given(postReadPort.findByIdList(List.of(postNo))).willReturn(List.of(mockPost));

        User mockUser = createMockUser();
        given(userReadPort.findByExpertNo(mockPost.getAuthorNo())).willReturn(mockUser);


        // when
        List<LoadWeeklyMagazinePostByWeekAtResult> results = service.load(weekAt);

        // then

        assertThat(results).hasSize(1)
                .extracting(
                        LoadWeeklyMagazinePostByWeekAtResult::postNo,
                        LoadWeeklyMagazinePostByWeekAtResult::authorNickname,
                        LoadWeeklyMagazinePostByWeekAtResult::postTitle,
                        LoadWeeklyMagazinePostByWeekAtResult::thumbnailUrl
                ).containsExactlyInAnyOrder(
                        tuple(mockPost.getPostNo(), mockUser.getNickname(), mockPost.getTitle(), "") // TODO: file key -> url 변환 로직
                );

        then(weeklyMagazineReadPort).should().findWeeklyMagazinePostByWeekAt(
                weekAt.getYear(), weekAt.getMonth(), weekAt.getWeek());
        then(postReadPort).should().findByIdList(List.of(postNo));
        then(userReadPort).should().findByExpertNo(mockPost.getAuthorNo());
    }

    private WeeklyMagazinePost createMockWeeklyMagazinePost(String postNo) {
        return WeeklyMagazinePost.builder()
                .postNo(postNo)
                .build();
    }

    private Post createMockPost(String postNo) {
        return Post.builder()
                .postNo(postNo)
                .title("title")
                .authorNo("user-123")
                .build();
    }

    private User createMockUser() {
        return User.builder()
                .nickname("nickname")
                .build();
    }

}