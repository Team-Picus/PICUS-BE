package com.picus.core.price.domain.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OptionTest {

    @Test
    @DisplayName("Option을 수정한다.")
    public void updateOption() throws Exception {
        // given
        Option option = createOption("opt_no1", "opt_name1", 3, 10000, List.of("content1"));

        // when
        option.updateOption("new_name", 5, 20000, List.of("new_content"));

        // then
        assertThat(option).extracting(
                Option::getOptionNo,
                Option::getName,
                Option::getCount,
                Option::getPrice,
                Option::getContents
        ).containsExactly(
                "opt_no1", "new_name", 5, 20000, List.of("new_content")
        );
    }

    private Option createOption(String optionNo, String name, int count, int price, List<String> contents) {
        return Option.builder()
                .optionNo(optionNo)
                .name(name)
                .count(count)
                .price(price)
                .contents(contents)
                .build();
    }

}