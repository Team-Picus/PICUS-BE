package com.picus.core.price.adapter.out.persistence.mapper;

import com.picus.core.price.adapter.out.persistence.entity.OptionEntity;
import com.picus.core.price.domain.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OptionPersistenceMapperTest {

    private final OptionPersistenceMapper mapper = new OptionPersistenceMapper();

    @Test
    void toDomain_shouldMapFieldsCorrectly() {
        // given
        OptionEntity entity = OptionEntity.builder()
                .optionNo("opt_no")
                .name("옵션A")
                .count(2)
                .price(5000)
                .contents(List.of("기본 제공 1", "기본 제공 2"))
                .build();

        // when
        Option result = mapper.toDomain(entity);

        // then
        assertThat(result.getOptionNo()).isEqualTo("opt_no");
        assertThat(result.getName()).isEqualTo("옵션A");
        assertThat(result.getCount()).isEqualTo(2);
        assertThat(result.getPrice()).isEqualTo(5000);
        assertThat(result.getContents()).isEqualTo(List.of("기본 제공 1", "기본 제공 2"));
    }

    @Test
    @DisplayName("Option -> OptionEntity 매핑")
    public void toEntity() throws Exception {
        // given
        Option option = Option.builder()
                .name("옵션A")
                .count(2)
                .price(5000)
                .contents(List.of("기본 제공 1", "기본 제공 2"))
                .build();

        // when
        OptionEntity entity = mapper.toEntity(option);

        // then
        assertThat(entity.getName()).isEqualTo("옵션A");
        assertThat(entity.getCount()).isEqualTo(2);
        assertThat(entity.getPrice()).isEqualTo(5000);
        assertThat(entity.getContents()).isEqualTo(List.of("기본 제공 1", "기본 제공 2"));
    }
}