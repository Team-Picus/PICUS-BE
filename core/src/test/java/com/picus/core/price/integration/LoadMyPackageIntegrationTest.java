package com.picus.core.price.integration;

import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.price.adapter.in.web.data.response.LoadMyPackageResponse;
import com.picus.core.price.adapter.out.persistence.entity.PackageEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import com.picus.core.price.adapter.out.persistence.repository.PackageJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceJpaRepository;
import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.domain.vo.SnapSubTheme;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.picus.core.price.domain.vo.PriceThemeType.SNAP;
import static com.picus.core.price.domain.vo.SnapSubTheme.ADMISSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
public class LoadMyPackageIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private PriceJpaRepository priceJpaRepository;
    @Autowired
    private PackageJpaRepository packageJpaRepository;

    @AfterEach
    void tearDown() {
        packageJpaRepository.deleteAllInBatch();
        priceJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 자신의 패키지 정보를 조회할 수 있다.")
    public void loadMyPackage() throws Exception {
        // given - 데이터베이스에 데이터 셋팅
        UserEntity userEntity = createUserEntity();
        userEntity.assignExpertNo(userEntity.getUserNo());
        PriceEntity priceEntity = createPriceEntity(userEntity.getUserNo(), SNAP, ADMISSION);
        createPackageEntity(priceEntity, "pkg1");
        createPackageEntity(priceEntity, "pkg2");
        commitTestTransaction();

        // given - 요청 셋팅
        HttpEntity<Object> request = settingWebRequest(userEntity, null);

        // when
        ResponseEntity<BaseResponse<LoadMyPackageResponse>> response = restTemplate.exchange(
                "/api/v1/experts/prices/packages/me",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BaseResponse<LoadMyPackageResponse> body = response.getBody();
        assertThat(body).isNotNull();

        LoadMyPackageResponse result = body.getResult();
        assertThat(result).isNotNull();
        assertThat(result.prices()).hasSize(1);

        LoadMyPackageResponse.PriceResponse price = result.prices().getFirst();
        assertThat(price.priceThemeType()).isEqualTo(SNAP);
        assertThat(price.snapSubTheme()).isEqualTo(ADMISSION);

        assertThat(price.packages())
                .hasSize(2)
                .extracting(LoadMyPackageResponse.PriceResponse.PackageResponse::name)
                .containsExactlyInAnyOrder("pkg1", "pkg2");

        // 각 패키지의 식별자가 비어있지 않은지 확인
        assertThat(price.packages()).allSatisfy(p -> assertThat(p.packageNo()).isNotBlank());
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
                .build();
        return userJpaRepository.save(userEntity);
    }

    private PriceEntity createPriceEntity(String expertNo, PriceThemeType priceThemeType, SnapSubTheme snapSubTheme) {
        PriceEntity priceEntity = PriceEntity.builder()
                .expertNo(expertNo)
                .priceThemeType(priceThemeType)
                .snapSubTheme(snapSubTheme)
                .build();
        return priceJpaRepository.save(priceEntity);
    }
    private PackageEntity createPackageEntity(PriceEntity priceEntity, String name) {
        PackageEntity pkgEntity = PackageEntity.builder()
                .priceEntity(priceEntity)
                .name(name)
                .price(0)
                .contents(List.of())
                .notice("")
                .build();
        return packageJpaRepository.save(pkgEntity);
    }

    private <T> HttpEntity<T> settingWebRequest(UserEntity userEntity, T webRequest) {
        String accessToken = tokenProvider.createAccessToken(userEntity.getUserNo(), userEntity.getRole().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Bearer " + accessToken);
        return new HttpEntity<>(webRequest, headers);
    }
    private void commitTestTransaction() {
        TestTransaction.flagForCommit();  // 지금까지 열린 테스트 트랜잭션을 커밋
        TestTransaction.end(); // 실제 커밋 수행
    }
}
