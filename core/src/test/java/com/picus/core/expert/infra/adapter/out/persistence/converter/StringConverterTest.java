package com.picus.core.expert.infra.adapter.out.persistence.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StringConverterTest {

    @Test
    @DisplayName("문자열 리스트(Entity 데이터)를 ,를 기준으로 하나의 문자열(데이터베이스 컬럼)으로 변환한다. - 문자열이 여러 개")
    public void convertToDatabaseColumn() throws Exception {
        // given
        StringConverter stringConverter = new StringConverter();
        List<String> entityLinkList =
                List.of(
                        "www.instagram.com/test_account",
                        "blog.tistory.com",
                        "www.youtube.com/channel");

        // when
        String converted = stringConverter.convertToDatabaseColumn(entityLinkList);

        // then
        assertThat(converted)
                .isEqualTo("www.instagram.com/test_account,blog.tistory.com,www.youtube.com/channel");
    }

    @Test
    @DisplayName("하나의 문자열(데이터베이스 컬럼)을 포트폴리오 링크 리스트(Entity 데이터)으로 변환한다. - 문자열이 여러 개")
    public void convertToEntityAttribute() throws Exception {
        // given
        StringConverter stringConverter = new StringConverter();
        String dbLinkString = "www.instagram.com/test_account,blog.tistory.com,www.youtube.com/channel";

        // when
        List<String> entityLinkList = stringConverter.convertToEntityAttribute(dbLinkString);

        // then
        assertThat(entityLinkList).hasSize(3)
                .containsExactly("www.instagram.com/test_account",
                        "blog.tistory.com",
                        "www.youtube.com/channel"
                );
    }


    @Test
    @DisplayName("문자열 리스트(Entity 데이터)를 ,를 기준으로 하나의 문자열(데이터베이스 컬럼)으로 변환한다. - 문자열이 한 개")
    public void convertToDatabaseColumnWithOneString() throws Exception {
        // given
        StringConverter stringConverter = new StringConverter();
        List<String> entityLinkList =
                List.of(
                        "www.instagram.com/test_account");

        // when
        String converted = stringConverter.convertToDatabaseColumn(entityLinkList);

        // then
        assertThat(converted)
                .isEqualTo("www.instagram.com/test_account");
    }

    @Test
    @DisplayName("하나의 문자열(데이터베이스 컬럼)을 포트폴리오 링크 리스트(Entity 데이터)으로 변환한다. - 문자열이 한 개")
    public void convertToEntityAttributeWithOneString() throws Exception {
        // given
        StringConverter stringConverter = new StringConverter();
        String dbLinkString = "www.instagram.com/test_account";

        // when
        List<String> entityLinkList = stringConverter.convertToEntityAttribute(dbLinkString);

        // then
        assertThat(entityLinkList).hasSize(1)
                .containsExactly("www.instagram.com/test_account");
    }

}