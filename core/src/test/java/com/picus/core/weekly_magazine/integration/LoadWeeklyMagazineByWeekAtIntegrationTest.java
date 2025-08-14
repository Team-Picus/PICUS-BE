package com.picus.core.weekly_magazine.integration;

import com.picus.core.shared.IntegrationTestSupport;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.Role;
import com.picus.core.weekly_magazine.adapter.in.web.data.response.LoadWeeklyMagazineByWeekAtResponse;
import com.picus.core.weekly_magazine.adapter.out.persistence.entity.WeeklyMagazineEntity;
import com.picus.core.weekly_magazine.adapter.out.persistence.entity.vo.WeekAt;
import com.picus.core.weekly_magazine.adapter.out.persistence.repository.WeeklyMagazineJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

public class LoadWeeklyMagazineByWeekAtIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private WeeklyMagazineJpaRepository weeklyMagazineJpaRepository;

    @AfterEach
    void tearDown() {
        weeklyMagazineJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 특정 주차의 주간 매거진을 조회할 수 있다.")
    public void load() throws Exception {
        // given - 데이터베이스에 데이터 셋팅
        int year = 2025;
        int month = 10;
        int week = 2;

        WeeklyMagazineEntity weeklyMagazineEntity =
                createWeeklyMagazineEntity("topic", "topic_desc", year, month, week);
        commitTestTransaction();

        // then - 요청 셋팅
        HttpEntity<Object> httpEntity = settingWebRequest(createUserEntity(), null);

        // when
        ResponseEntity<BaseResponse<LoadWeeklyMagazineByWeekAtResponse>> response = restTemplate.exchange(
                "/api/v1/weekly_magazine/{year}/{month}/{week}",
                GET,
                httpEntity,
                new ParameterizedTypeReference<>() {
                },
                year, month, week
        );
        
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BaseResponse<LoadWeeklyMagazineByWeekAtResponse> body = response.getBody();
        assertThat(body).isNotNull();

        LoadWeeklyMagazineByWeekAtResponse result = body.getResult();
        assertThat(result.topic()).isEqualTo(weeklyMagazineEntity.getTopic());
        assertThat(result.topicDescription()).isEqualTo(weeklyMagazineEntity.getTopicDescription());
        assertThat(result.weekAt().getYear()).isEqualTo(weeklyMagazineEntity.getWeekAt().getYear());
        assertThat(result.weekAt().getMonth()).isEqualTo(weeklyMagazineEntity.getWeekAt().getMonth());
        assertThat(result.weekAt().getWeek()).isEqualTo(weeklyMagazineEntity.getWeekAt().getWeek());
        assertThat(result.thumbnailUrl()).isEqualTo(""); // TODO: filekey->url 로직 필요
    }

    private UserEntity createUserEntity() {
        UserEntity userEntity = UserEntity.builder()
                .name("이름")
                .nickname("nickname")
                .tel("01012345678")
                .role(Role.CLIENT)
                .email("email@example.com")
                .providerId("social_abc123")
                .provider(Provider.KAKAO)
                .reservationHistoryCount(5)
                .followCount(10)
                .myMoodboardCount(2)
                .expertNo("expertNo")
                .build();
        return userJpaRepository.save(userEntity);
    }
    private WeeklyMagazineEntity createWeeklyMagazineEntity(String topic, String topicDesc, int year, int month, int week) {
        WeeklyMagazineEntity weeklyMagazineEntity = WeeklyMagazineEntity.builder()
                .topic(topic)
                .topicDescription(topicDesc)
                .weekAt(createWeekAt(year, month, week))
                .thumbnailKey("t.jpg")
                .build();
        return weeklyMagazineJpaRepository.save(weeklyMagazineEntity);
    }

    private WeekAt createWeekAt(int year, int month, int week) {
        return WeekAt.builder()
                .year(year)
                .month(month)
                .week(week)
                .build();
    }
}
