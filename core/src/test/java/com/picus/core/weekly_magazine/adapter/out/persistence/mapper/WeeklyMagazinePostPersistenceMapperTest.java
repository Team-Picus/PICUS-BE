package com.picus.core.weekly_magazine.adapter.out.persistence.mapper;

import com.picus.core.weekly_magazine.adapter.out.persistence.entity.WeeklyMagazinePostEntity;
import com.picus.core.weekly_magazine.domain.model.WeeklyMagazinePost;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeeklyMagazinePostPersistenceMapperTest {

    private final WeeklyMagazinePostPersistenceMapper magazinePostMapper = new WeeklyMagazinePostPersistenceMapper();

    @Test
    @DisplayName("WeeklyMagazinePostEntity -> WeeklyMagazinePost 매핑")
    public void toDomain() throws Exception {
        // given
        WeeklyMagazinePostEntity entity = WeeklyMagazinePostEntity.builder()
                .weeklyMagazinePostNo("wmp-123")
                .postNo("post-123")
                .build();

        // when
        WeeklyMagazinePost domain = magazinePostMapper.toDomain(entity);

        // then
        Assertions.assertThat(domain.getWeeklyMagazinePostNo()).isEqualTo(entity.getWeeklyMagazinePostNo());
        Assertions.assertThat(domain.getPostNo()).isEqualTo(entity.getPostNo());
    }

}