package com.picus.core.post.infra.adapter.out.persistence.converter;

import com.picus.core.post.domain.model.vo.PostThemeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.picus.core.post.domain.model.vo.PostThemeType.*;
import static org.assertj.core.api.Assertions.assertThat;

class PostThemeTypeConverterTest {

    @Test
    @DisplayName("PostThemeType 리스트를 ,를 기준으로 하나의 문자열로 이어붙인다.")
    public void convertToDatabaseColumn() throws Exception {
        // given
        PostThemeTypeConverter postThemeTypeConverter = new PostThemeTypeConverter();
        List<PostThemeType> postThemeTypes = List.of(FASHION, BEAUTY, EVENT);

        // when
        String converted = postThemeTypeConverter.convertToDatabaseColumn(postThemeTypes);

        // then
        assertThat(converted)
                .isEqualTo("FASHION,BEAUTY,EVENT");
    }

    @Test
    @DisplayName(",를 기준으로 붙어져 있는 PostThemeType 문자열을 리스트로 변환한다.")
    public void convertToEntityAttribute() throws Exception {
        // given
        PostThemeTypeConverter postThemeTypeConverter = new PostThemeTypeConverter();
        String themeTypeString = "FASHION,BEAUTY,EVENT";

        // when
        List<PostThemeType> postThemeTypes = postThemeTypeConverter.convertToEntityAttribute(themeTypeString);

        // then
        assertThat(postThemeTypes).hasSize(3)
                .containsExactly(FASHION, BEAUTY, EVENT);
    }
}