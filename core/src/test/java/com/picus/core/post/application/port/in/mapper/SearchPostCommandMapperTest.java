package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.adapter.out.persistence.dto.SearchPostCond;
import com.picus.core.post.application.port.in.command.SearchPostCommand;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SearchPostCommandMapperTest {

    private SearchPostCommandMapper commandMapper = new SearchPostCommandMapper();

    @Test
    @DisplayName("SearchPostCommand -> SearchPostCond 매핑")
    public void toCond() throws Exception {
        // given
        SearchPostCommand searchPostCommand = new SearchPostCommand(
                List.of(PostThemeType.SNAP),
                List.of(SnapSubTheme.FAMILY),
                SpaceType.OUTDOOR,
                "서울 강남구",
                List.of(PostMoodType.COZY),
                "keyword",
                "updatedAt",
                "DESC",
                "post-123",
                20
        );

        // when
        SearchPostCond cond = commandMapper.toCond(searchPostCommand);

        // then
        assertThat(cond.themeTypes()).containsExactly(PostThemeType.SNAP);
        assertThat(cond.snapSubThemes()).containsExactly(SnapSubTheme.FAMILY);
        assertThat(cond.spaceType()).isEqualTo(SpaceType.OUTDOOR);
        assertThat(cond.address()).isEqualTo("서울 강남구");
        assertThat(cond.moodTypes()).containsExactly(PostMoodType.COZY);
        assertThat(cond.keyword()).isEqualTo("keyword");
    }

}