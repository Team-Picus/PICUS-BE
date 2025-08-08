package com.picus.core.weekly_magazine.adapter.in.web.mapper;

import com.picus.core.weekly_magazine.adapter.in.web.data.response.LoadWeeklyMagazinePostByWeekAtResponse;
import com.picus.core.weekly_magazine.application.port.in.result.LoadWeeklyMagazinePostByWeekAtResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LoadWeeklyMagazinePostByWeekAtWebMapperTest {

    private final LoadWeeklyMagazinePostByWeekAtWebMapper mapper = new LoadWeeklyMagazinePostByWeekAtWebMapper();

    @Test
    void toResponse() {
        // given
        LoadWeeklyMagazinePostByWeekAtResult result1 = LoadWeeklyMagazinePostByWeekAtResult.builder()
                .postNo("POST001")
                .authorNickname("홍길동")
                .postTitle("제목1")
                .thumbnailUrl("http://image.com/1.jpg")
                .build();

        LoadWeeklyMagazinePostByWeekAtResult result2 = LoadWeeklyMagazinePostByWeekAtResult.builder()
                .postNo("POST002")
                .authorNickname("임꺽정")
                .postTitle("제목2")
                .thumbnailUrl("http://image.com/2.jpg")
                .build();

        List<LoadWeeklyMagazinePostByWeekAtResult> results = List.of(result1, result2);

        // when
        LoadWeeklyMagazinePostByWeekAtResponse response = mapper.toResponse(results);

        // then
        assertThat(response).isNotNull();
        assertThat(response.posts()).hasSize(2);

        LoadWeeklyMagazinePostByWeekAtResponse.PostResponse post1 = response.posts().get(0);
        assertThat(post1.postNo()).isEqualTo("POST001");
        assertThat(post1.authorNickname()).isEqualTo("홍길동");
        assertThat(post1.postTitle()).isEqualTo("제목1");
        assertThat(post1.thumbnailUrl()).isEqualTo("http://image.com/1.jpg");

        LoadWeeklyMagazinePostByWeekAtResponse.PostResponse post2 = response.posts().get(1);
        assertThat(post2.postNo()).isEqualTo("POST002");
        assertThat(post2.authorNickname()).isEqualTo("임꺽정");
        assertThat(post2.postTitle()).isEqualTo("제목2");
        assertThat(post2.thumbnailUrl()).isEqualTo("http://image.com/2.jpg");
    }
}