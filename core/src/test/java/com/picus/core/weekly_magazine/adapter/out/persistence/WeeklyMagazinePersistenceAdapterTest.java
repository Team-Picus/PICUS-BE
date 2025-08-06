package com.picus.core.weekly_magazine.adapter.out.persistence;

import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.adapter.out.persistence.repository.PostJpaRepository;
import com.picus.core.post.domain.vo.SpaceType;
import com.picus.core.weekly_magazine.adapter.out.persistence.entity.WeeklyMagazineEntity;
import com.picus.core.weekly_magazine.adapter.out.persistence.entity.vo.WeekAt;
import com.picus.core.weekly_magazine.adapter.out.persistence.mapper.WeeklyMagazinePersistenceMapper;
import com.picus.core.weekly_magazine.adapter.out.persistence.repository.WeeklyMagazineJpaRepository;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazine;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static com.picus.core.post.domain.vo.PostMoodType.VINTAGE;
import static com.picus.core.post.domain.vo.PostThemeType.BEAUTY;
import static org.assertj.core.api.Assertions.assertThat;

@Import({
        WeeklyMagazinePersistenceAdapter.class,
        WeeklyMagazinePersistenceMapper.class
})
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WeeklyMagazinePersistenceAdapterTest {

    @Autowired
    EntityManager em;
    @Autowired
    private PostJpaRepository postJpaRepository;
    @Autowired
    private WeeklyMagazineJpaRepository weeklyMagazineJpaRepository;
    @Autowired
    private WeeklyMagazinePersistenceAdapter weeklyMagazinePersistenceAdapter;

    @Test
    @DisplayName("특정 WeekAt을 가진 WeeklyMagazine을 조회한다.")
    public void findByWeekAt() throws Exception {
        // given - 데이터베이스에 데이터 셋팅
        int year = 2025;
        int month = 7;
        int week = 2;
        WeeklyMagazineEntity weeklyMagazineEntity =
                createWeeklyMagazineEntity("topic", "topic_desc", year, month, week);
        clearPersistenceContext();

        // when
        Optional<WeeklyMagazine> optionalWeeklyMagazine =
                weeklyMagazinePersistenceAdapter.findByWeekAt(year, month, week);

        // then
        assertThat(optionalWeeklyMagazine).isPresent();

        WeeklyMagazine weeklyMagazine = optionalWeeklyMagazine.get();
        assertThat(weeklyMagazine.getWeeklyMagazineNo()).isEqualTo(weeklyMagazineEntity.getWeeklyMagazineNo());
        assertThat(weeklyMagazine.getTopic()).isEqualTo(weeklyMagazineEntity.getTopic());
        assertThat(weeklyMagazine.getWeekAt().getYear()).isEqualTo(weeklyMagazineEntity.getWeekAt().getYear());
        assertThat(weeklyMagazine.getWeekAt().getMonth()).isEqualTo(weeklyMagazineEntity.getWeekAt().getMonth());
        assertThat(weeklyMagazine.getWeekAt().getWeek()).isEqualTo(weeklyMagazineEntity.getWeekAt().getWeek());
        assertThat(weeklyMagazine.getThumbnailKey()).isEqualTo(weeklyMagazineEntity.getThumbnailKey());
    }

    private WeeklyMagazineEntity createWeeklyMagazineEntity(String topic, String topicDesc, int year, int month, int week) {
        WeeklyMagazineEntity weeklyMagazineEntity = WeeklyMagazineEntity.builder()
                .topic(topic)
                .topicDescription(topicDesc)
                .weekAt(createWeekAt(year, month, week))
                .thumbnailKey("t.jpg")
                .build();
        return weeklyMagazineJpaRepository.save(weeklyMagazineEntity);
    }

    private WeekAt createWeekAt(int year, int month, int week) {
        return WeekAt.builder()
                .year(year)
                .month(month)
                .week(week)
                .build();
    }

    private PostEntity createPostEntity(String title) {
        PostEntity postEntity = PostEntity.builder()
                .packageNo("packageNo")
                .expertNo("expertNo")
                .title(title)
                .oneLineDescription("oneLineDescription")
                .detailedDescription("detailedDescription")
                .postThemeTypes(List.of(BEAUTY))
                .snapSubThemes(List.of())
                .postMoodTypes(List.of(VINTAGE))
                .spaceType(SpaceType.OUTDOOR)
                .spaceAddress("spaceAddress")
                .isPinned(false)
                .build();
        return postJpaRepository.save(postEntity);
    }

    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }
}