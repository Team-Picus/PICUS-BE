package com.picus.core.post.infra.adapter.out.persistence.converter;

import com.picus.core.post.domain.model.vo.PostMoodType;
import com.picus.core.post.domain.model.vo.PostThemeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.picus.core.post.domain.model.vo.PostMoodType.*;
import static org.assertj.core.api.Assertions.assertThat;

class PostMoodTypeConverterTest {

    @Test
    @DisplayName("PostMoodType 리스트를 ,를 기준으로 하나의 문자열로 이어붙인다.")
    public void convertToDatabaseColumn() throws Exception {
        // given
        PostMoodTypeConverter postMoodTypeConverter = new PostMoodTypeConverter();
        List<PostMoodType> postMoodTypes = List.of(URBAN, EXPERIMENTAL, INTENSE);

        // when
        String converted = postMoodTypeConverter.convertToDatabaseColumn(postMoodTypes);

        // then
        assertThat(converted)
                .isEqualTo("URBAN,EXPERIMENTAL,INTENSE");
    }

    @Test
    @DisplayName(",를 기준으로 붙어져 있는 PostMoodType 문자열을 리스트로 변환한다.")
    public void convertToEntityAttribute() throws Exception {
        // given
        PostMoodTypeConverter postMoodTypeConverter = new PostMoodTypeConverter();
        String moodTypeString = "URBAN,EXPERIMENTAL,INTENSE";

        // when
        List<PostMoodType> postMoodTypes = postMoodTypeConverter.convertToEntityAttribute(moodTypeString);

        // then
        assertThat(postMoodTypes).hasSize(3)
                .containsExactly(URBAN, EXPERIMENTAL, INTENSE);
    }

}