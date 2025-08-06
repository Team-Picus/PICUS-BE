package com.picus.core.weekly_magazine.adapter.out.persistence;

import com.picus.core.weekly_magazine.adapter.out.persistence.entity.WeeklyMagazineEntity;
import com.picus.core.weekly_magazine.adapter.out.persistence.entity.WeeklyMagazinePostEntity;
import com.picus.core.weekly_magazine.adapter.out.persistence.entity.vo.WeekAt;
import com.picus.core.weekly_magazine.adapter.out.persistence.mapper.WeeklyMagazinePersistenceMapper;
import com.picus.core.weekly_magazine.adapter.out.persistence.mapper.WeeklyMagazinePostPersistenceMapper;
import com.picus.core.weekly_magazine.adapter.out.persistence.repository.WeeklyMagazineJpaRepository;
import com.picus.core.weekly_magazine.adapter.out.persistence.repository.WeeklyMagazinePostJpaRepository;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazine;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazinePost;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Import({
        WeeklyMagazinePersistenceAdapter.class,
        WeeklyMagazinePersistenceMapper.class,
        WeeklyMagazinePostPersistenceMapper.class
})
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WeeklyMagazinePersistenceAdapterTest {

    @Autowired
    private WeeklyMagazinePersistenceAdapter persistenceAdapter;

    @Autowired
    private WeeklyMagazineJpaRepository weeklyMagazineJpaRepository;
    @Autowired
    private WeeklyMagazinePostJpaRepository weeklyMagazinePostJpaRepository;

    @Autowired
    EntityManager em;

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
                persistenceAdapter.findByWeekAt(year, month, week);

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

    @Test
    @DisplayName("특정 weekAt을 가진 주간 매거진의 게시물을 조회한다.")
    public void findWeeklyMagazinePostByWeekAt() throws Exception {
        // given - 데이터베이스에 데이터 셋팅
        int year = 2025;
        int month = 7;
        int week = 2;
        WeeklyMagazineEntity magazineEntity =
                createWeeklyMagazineEntity("topic", "topic_desc", year, month, week);
        WeeklyMagazinePostEntity magazinePostEntity = createWeeklyMagazinePostEntity(magazineEntity, "post-123");
        clearPersistenceContext();

        // when
        List<WeeklyMagazinePost> weeklyMagazinePosts = persistenceAdapter.findWeeklyMagazinePostByWeekAt(year, month, week);

        // then
        assertThat(weeklyMagazinePosts).hasSize(1)
                .extracting(WeeklyMagazinePost::getWeeklyMagazinePostNo, WeeklyMagazinePost::getPostNo)
                .containsExactlyInAnyOrder(tuple(magazinePostEntity.getWeeklyMagazinePostNo(), magazinePostEntity.getPostNo()));
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

    private WeeklyMagazinePostEntity createWeeklyMagazinePostEntity(WeeklyMagazineEntity weeklyMagazineEntity, String postNo) {
        WeeklyMagazinePostEntity entity = WeeklyMagazinePostEntity.builder()
                .weeklyMagazineEntity(weeklyMagazineEntity)
                .postNo(postNo)
                .build();
        return weeklyMagazinePostJpaRepository.save(entity);
    }

    private WeekAt createWeekAt(int year, int month, int week) {
        return WeekAt.builder()
                .year(year)
                .month(month)
                .week(week)
                .build();
    }

    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }
}