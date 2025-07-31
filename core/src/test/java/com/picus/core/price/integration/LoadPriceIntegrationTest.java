package com.picus.core.price.integration;

import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.price.adapter.in.web.data.response.LoadPriceResponse;
import com.picus.core.price.adapter.out.persistence.entity.OptionEntity;
import com.picus.core.price.adapter.out.persistence.entity.PackageEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceReferenceImageEntity;
import com.picus.core.price.adapter.out.persistence.repository.OptionJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PackageJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceReferenceImageJpaRepository;
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

import static com.picus.core.price.adapter.in.web.data.response.LoadPriceResponse.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
public class LoadPriceIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    PriceJpaRepository priceJpaRepository;
    @Autowired
    PackageJpaRepository packageJpaRepository;
    @Autowired
    OptionJpaRepository optionJpaRepository;
    @Autowired
    PriceReferenceImageJpaRepository priceReferenceImageJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @AfterEach
    void tearDown() {
        priceReferenceImageJpaRepository.deleteAllInBatch();
        packageJpaRepository.deleteAllInBatch();
        optionJpaRepository.deleteAllInBatch();
        priceJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 특정 전문가의 가격정보를 조회할 수 있다.")
    public void getPricesByExpert_success() throws Exception {
        // given
        String expertNo = "expert001";
        PriceEntity priceEntity = createPriceEntity(expertNo, PriceThemeType.BEAUTY);
        PackageEntity packageEntity = createPackageEntity(priceEntity, "기본 패키지", 20000, List.of("헤어컷", "드라이"), "사전 예약 필수");
        OptionEntity optionEntity = createOptionEntity(priceEntity, "옵션 A", 1, 5000, List.of("마사지 추가"));
        PriceReferenceImageEntity referenceImageEntity = createReferenceImageEntity(priceEntity, "file-key-123", 1);

        commitTestTransaction();

        // when
        UserEntity userEntity = createUserEntity(); // 인증을 위한 가상의 사용자 생성
        String accessToken = tokenProvider.createAccessToken(userEntity.getUserNo(), userEntity.getRole().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<BaseResponse<List<LoadPriceResponse>>> response = restTemplate.exchange(
                "/api/v1/experts/{expert_no}/prices",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                },
                expertNo
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BaseResponse<List<LoadPriceResponse>> body = response.getBody();
        assertThat(body).isNotNull();

        List<LoadPriceResponse> result = body.getResult();

        assertThat(result).hasSize(1);

        LoadPriceResponse first = result.getFirst();
        assertThat(first.priceNo()).isEqualTo(priceEntity.getPriceNo());
        assertThat(first.priceThemeType()).isEqualTo("BEAUTY");

        // package
        List<PackageResponse> packageResult = first.packages();
        assertThat(packageResult).hasSize(1)
                .extracting(
                        PackageResponse::packageNo,
                        PackageResponse::name,
                        PackageResponse::price,
                        PackageResponse::contents,
                        PackageResponse::notice)
                .contains(tuple(
                        packageEntity.getPackageNo(),
                        "기본 패키지",
                        20000,
                        List.of("헤어컷", "드라이"),
                        "사전 예약 필수"));
        // option
        List<OptionResponse> optionResult = first.options();
        assertThat(optionResult).hasSize(1)
                .extracting(OptionResponse::optionNo,
                        OptionResponse::name,
                        OptionResponse::count,
                        OptionResponse::price,
                        OptionResponse::contents)
                .contains(tuple(
                        optionEntity.getOptionNo(),
                        "옵션 A",
                        1,
                        5000,
                        List.of("마사지 추가")));

        // PriceReferenceImage TODO: key -> url 후 재검증 필요
        List<PriceReferenceImageResponse> imageResults = first.priceReferenceImages();
        assertThat(imageResults).hasSize(1)
                .extracting(
                        PriceReferenceImageResponse::priceRefImageNo,
                        PriceReferenceImageResponse::fileKey,
                        PriceReferenceImageResponse::imageUrl,
                        PriceReferenceImageResponse::imageOrder)
                .contains(tuple(referenceImageEntity.getPriceReferenceImageNo(), "file-key-123", null, 1));
    }

    private PriceEntity createPriceEntity(String expertNo, PriceThemeType priceThemeType) {
        PriceEntity priceEntity = PriceEntity.builder()
                .expertNo(expertNo)
                .priceThemeType(priceThemeType)
                .build();
        return priceJpaRepository.save(priceEntity);
    }

    private PackageEntity createPackageEntity(PriceEntity priceEntity, String name, int price, List<String> contents, String notice) {
        PackageEntity pkgEntity = PackageEntity.builder()
                .priceEntity(priceEntity)
                .name(name)
                .price(price)
                .contents(contents)
                .notice(notice)
                .build();
        return packageJpaRepository.save(pkgEntity);
    }

    private OptionEntity createOptionEntity(PriceEntity priceEntity, String name, int count, int price, List<String> content) {
        OptionEntity optionEntity = OptionEntity.builder()
                .priceEntity(priceEntity)
                .name(name)
                .count(count)
                .price(price)
                .contents(content)
                .build();
        return optionJpaRepository.save(optionEntity);
    }

    private PriceReferenceImageEntity createReferenceImageEntity(PriceEntity priceEntity, String fileKey, int imageOrder) {
        PriceReferenceImageEntity referenceImageEntity = PriceReferenceImageEntity.builder()
                .priceEntity(priceEntity)
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .build();
        return priceReferenceImageJpaRepository.save(referenceImageEntity);
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
                .expertNo("expert-123")
                .build();
        return userJpaRepository.save(userEntity);
    }

    private void commitTestTransaction() {
        TestTransaction.flagForCommit();  // 지금까지 열린 테스트 트랜잭션을 커밋
        TestTransaction.end(); // 실제 커밋 수행
    }
}
