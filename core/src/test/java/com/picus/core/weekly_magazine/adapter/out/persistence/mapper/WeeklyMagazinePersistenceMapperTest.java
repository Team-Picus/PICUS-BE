package com.picus.core.weekly_magazine.adapter.out.persistence.mapper;

import com.picus.core.weekly_magazine.adapter.out.persistence.entity.WeeklyMagazineEntity;
import com.picus.core.weekly_magazine.adapter.out.persistence.entity.vo.WeekAt;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazine;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazinePost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

class WeeklyMagazinePersistenceMapperTest {

    private WeeklyMagazinePersistenceMapper persistenceMapper = new WeeklyMagazinePersistenceMapper();

    @Test
    @DisplayName("WeeklyMagazineEntity, List<WeeklyMagazinePost> -> WeeklyMagazine")
    public void toDomain() throws Exception {
        // given
        WeeklyMagazineEntity weeklyMagazineEntity = WeeklyMagazineEntity.builder()
                .weeklyMagazineNo("wm-123")
                .topic("topic")
                .topicDescription("topic_desc")
                .weekAt(WeekAt.builder()
                        .year(2020)
                        .month(10)
                        .week(2)
                        .build())
                .thumbnailKey("a.jpg")
                .build();

        List<WeeklyMagazinePost> weeklyMagazinePosts = List.of(WeeklyMagazinePost.builder()
                .weeklyMagazinePostNo("wmp-123")
                .postNo("post-123")
                .build());

        // when
        WeeklyMagazine domain = persistenceMapper.toDomain(weeklyMagazineEntity, weeklyMagazinePosts);

        // then
        assertThat(domain.getWeeklyMagazineNo()).isEqualTo("wm-123");
        assertThat(domain.getTopic()).isEqualTo("topic");
        assertThat(domain.getTopicDescription()).isEqualTo("topic_desc");
        assertThat(domain.getWeekAt().getYear()).isEqualTo(2020);
        assertThat(domain.getWeekAt().getMonth()).isEqualTo(10);
        assertThat(domain.getWeekAt().getWeek()).isEqualTo(2);
        assertThat(domain.getThumbnailKey()).isEqualTo("a.jpg");
        assertThat(domain.getWeeklyMagazinePosts())
                .extracting(
                        WeeklyMagazinePost::getWeeklyMagazinePostNo,
                        WeeklyMagazinePost::getPostNo
                )
                .containsExactly(tuple("wmp-123", "post-123"));
    }

}