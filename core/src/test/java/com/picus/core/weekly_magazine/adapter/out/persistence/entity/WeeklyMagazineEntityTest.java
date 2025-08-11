package com.picus.core.weekly_magazine.adapter.out.persistence.entity;

import com.picus.core.weekly_magazine.adapter.out.persistence.entity.vo.WeekAt;
import com.picus.core.weekly_magazine.adapter.out.persistence.repository.WeeklyMagazineJpaRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WeeklyMagazineEntityTest {

    @Autowired
    private WeeklyMagazineJpaRepository weeklyMagazineJpaRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("year, month, week가 동일한 WeeklyMagazineEntity가 존재할 수 없다.")
    public void weekAt_unique() throws Exception {
        // given
        createWeeklyMagazineEntity("t1", "td1", 2025, 10, 2);

        // when // then
        assertThatThrownBy(() -> {
            createWeeklyMagazineEntity("t2", "td2", 2025, 10, 2);
            clearPersistenceContext();
        })
                .isInstanceOf(ConstraintViolationException.class);
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

    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }

}