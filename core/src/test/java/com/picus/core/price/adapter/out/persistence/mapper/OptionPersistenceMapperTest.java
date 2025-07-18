package com.picus.core.price.adapter.out.persistence.mapper;

import com.picus.core.price.adapter.out.persistence.entity.OptionEntity;
import com.picus.core.price.domain.model.Option;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OptionPersistenceMapperTest {

    private final OptionPersistenceMapper mapper = new OptionPersistenceMapper();

    @Test
    void toDomain_shouldMapFieldsCorrectly() {
        // given
        OptionEntity entity = OptionEntity.builder()
                .name("옵션A")
                .count(2)
                .price(5000)
                .content(List.of("기본 제공 1", "기본 제공 2"))
                .build();

        // when
        Option result = mapper.toDomain(entity);

        // then
        assertThat(result.getName()).isEqualTo("옵션A");
        assertThat(result.getCount()).isEqualTo(2);
        assertThat(result.getPrice()).isEqualTo(5000);
        assertThat(result.getContents()).isEqualTo(List.of("기본 제공 1", "기본 제공 2"));
    }
}