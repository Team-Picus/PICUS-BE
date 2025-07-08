package com.picus.core.expert.infra.adapter.out.persistence.converter;

import com.picus.core.expert.domain.model.vo.ActivityArea;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.picus.core.expert.domain.model.vo.ActivityArea.*;
import static org.assertj.core.api.Assertions.assertThat;

class ActivityAreasConverterTest {

    @Test
    @DisplayName("ActivityArea 리스트를 ,를 기준으로 하나의 문자열로 이어붙인다.")
    public void convertToDatabaseColumn() throws Exception {
        // given
        ActivityAreasConverter activityAreasConverter = new ActivityAreasConverter();
        List<ActivityArea> activityAreas = List.of(SEOUL_GANGBUKGU, SEOUL_GANGNAMGU, SEOUL_GANGSEOGU);

        // when
        String converted = activityAreasConverter.convertToDatabaseColumn(activityAreas);

        // then
        assertThat(converted)
                .isEqualTo("SEOUL_GANGBUKGU,SEOUL_GANGNAMGU,SEOUL_GANGSEOGU");
    }

    @Test
    @DisplayName(",를 기준으로 붙어져 있는 ActivityArea 문자열을 리스트로 변환한다.")
    public void convertToEntityAttribute() throws Exception {
        // given
        ActivityAreasConverter activityAreasConverter = new ActivityAreasConverter();
        String activityAreaString = "SEOUL_GANGBUKGU,SEOUL_GANGNAMGU,SEOUL_GANGSEOGU";

        // when
        List<ActivityArea> activityAreas = activityAreasConverter.convertToEntityAttribute(activityAreaString);

        // then
        assertThat(activityAreas).hasSize(3)
                .containsExactly(SEOUL_GANGBUKGU, SEOUL_GANGNAMGU, SEOUL_GANGSEOGU);
    }

}