package com.picus.core.price.integration;

import com.picus.core.expert.domain.vo.PriceThemeType;
import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.price.adapter.in.web.data.response.GetPricesByExpertWebResponse;
import com.picus.core.price.adapter.out.persistence.entity.OptionEntity;
import com.picus.core.price.adapter.out.persistence.entity.PackageEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceReferenceImageEntity;
import com.picus.core.price.adapter.out.persistence.repository.OptionJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PackageJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceReferenceImageJpaRepository;
import com.picus.core.shared.common.BaseResponse;
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

import static com.picus.core.price.adapter.in.web.data.response.GetPricesByExpertWebResponse.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
public class GetPricesByExpertIntegrationTest {

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
        String accessToken = tokenProvider.createAccessToken("test_id", "ROLE_USER");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<BaseResponse<List<GetPricesByExpertWebResponse>>> response = restTemplate.exchange(
                "/api/v1/experts/{expert_no}/prices",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                },
                expertNo
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BaseResponse<List<GetPricesByExpertWebResponse>> body = response.getBody();
        assertThat(body).isNotNull();

        List<GetPricesByExpertWebResponse> result = body.getResult();

        assertThat(result).hasSize(1);

        GetPricesByExpertWebResponse first = result.getFirst();
        assertThat(first.priceNo()).isEqualTo(priceEntity.getPriceNo());
        assertThat(first.priceThemeType()).isEqualTo("BEAUTY");

        // package
        List<PackageWebResponse> packageResult = first.packages();
        assertThat(packageResult).hasSize(1)
                .extracting(
                        PackageWebResponse::packageNo,
                        PackageWebResponse::name,
                        PackageWebResponse::price,
                        PackageWebResponse::contents,
                        PackageWebResponse::notice)
                .contains(tuple(
                        packageEntity.getPackageNo(),
                        "기본 패키지",
                        20000,
                        List.of("헤어컷", "드라이"),
                        "사전 예약 필수"));
        // option
        List<OptionWebResponse> optionResult = first.options();
        assertThat(optionResult).hasSize(1)
                .extracting(OptionWebResponse::optionNo,
                        OptionWebResponse::name,
                        OptionWebResponse::count,
                        OptionWebResponse::price,
                        OptionWebResponse::contents)
                .contains(tuple(
                        optionEntity.getOptionNo(),
                        "옵션 A",
                        1,
                        5000,
                        List.of("마사지 추가")));

        // PriceReferenceImage TODO: key -> url 후 재검증 필요
        List<PriceReferenceImageWebResponse> imageResults = first.priceReferenceImages();
        assertThat(imageResults).hasSize(1)
                .extracting(PriceReferenceImageWebResponse::priceRefImageNo,
                        PriceReferenceImageWebResponse::imageUrl,
                        PriceReferenceImageWebResponse::imageOrder)
                .contains(tuple(referenceImageEntity.getPriceReferenceImageNo(), null, 1));
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

    private void commitTestTransaction() {
        TestTransaction.flagForCommit();  // 지금까지 열린 테스트 트랜잭션을 커밋
        TestTransaction.end(); // 실제 커밋 수행
    }
}
