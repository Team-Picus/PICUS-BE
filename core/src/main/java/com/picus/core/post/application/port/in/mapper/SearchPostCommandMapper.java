package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.adapter.out.persistence.dto.SearchPostCond;
import com.picus.core.post.application.port.in.command.SearchPostCommand;
import org.springframework.stereotype.Component;

@Component
public class SearchPostCommandMapper {

    public SearchPostCond toCond(SearchPostCommand command) {
        return SearchPostCond.builder()
                .themeTypes(command.themeTypes())
                .snapSubThemes(command.snapSubThemes())
                .spaceType(command.spaceType())
                .address(command.address())
                .moodTypes(command.moodTypes())
                .keyword(command.keyword())
                .build();
    }
}
