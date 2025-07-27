package com.picus.core.price.integration;

import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.expert.adapter.out.persistence.repository.ExpertJpaRepository;
import com.picus.core.expert.domain.vo.ApprovalStatus;
import com.picus.core.expert.domain.vo.PriceThemeType;
import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.price.adapter.in.web.data.request.*;
import com.picus.core.price.adapter.out.persistence.entity.OptionEntity;
import com.picus.core.price.adapter.out.persistence.entity.PackageEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceReferenceImageEntity;
import com.picus.core.price.adapter.out.persistence.repository.OptionJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PackageJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceReferenceImageJpaRepository;
import com.picus.core.price.application.port.in.request.ChangeStatus;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.picus.core.expert.domain.vo.PriceThemeType.BEAUTY;
import static com.picus.core.expert.domain.vo.PriceThemeType.FASHION;
import static com.picus.core.price.application.port.in.request.ChangeStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
public class ApplyChangesIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    ExpertJpaRepository expertJpaRepository;
    @Autowired
    PriceJpaRepository priceJpaRepository;
    @Autowired
    PriceReferenceImageJpaRepository priceReferenceImageJpaRepository;
    @Autowired
    PackageJpaRepository packageJpaRepository;
    @Autowired
    OptionJpaRepository optionJpaRepository;

    @AfterEach
    void tearDown() {
        priceReferenceImageJpaRepository.deleteAllInBatch();
        packageJpaRepository.deleteAllInBatch();
        optionJpaRepository.deleteAllInBatch();
        priceJpaRepository.deleteAllInBatch();
        expertJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 가격정보를 추가 할 수 있다.")
    public void applyChanges_save() throws Exception {
        // given

        // 데이터베이스 데이터 셋팅
        UserEntity userEntity = createUserEntity();

        ExpertEntity expertEntity = createExpertEntity(userEntity);
        userEntity.assignExpertNo(expertEntity.getExpertNo());


        commitTestTransaction();

        // 요청 값 셋팅
        UpdatePriceReferenceImageWebReq newImgWebReq =
                createPriceRefImageWebRequest(null, "new_file_key", 1, NEW); // 새로 추가

        UpdatePackageWebReq newPkgWebReq =
                createPackageWebRequest(null, "new_pkg_name", 10, List.of("new_cnt"),
                        "new_notice", NEW); // 추가

        UpdateOptionWebReq newOptWebReq =
                createOptionWebRequest(null, "new_opt_name", 2, 10,
                        List.of("new_cnt"), NEW); // 추가

        UpdatePriceWebReq updatePriceWebRequest =
                createPriceWebRequest(null, "FASHION", NEW,
                        List.of(newImgWebReq), List.of(newPkgWebReq), List.of(newOptWebReq));

        UpdatePriceListWebReq webRequest = new UpdatePriceListWebReq(List.of(updatePriceWebRequest));

        HttpEntity<UpdatePriceListWebReq> request = settingWebRequest(userEntity.getUserNo(), webRequest);

        // when
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/v1/experts/prices",
                HttpMethod.PATCH,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Price 검증
        List<PriceEntity> savedPriceEntity = priceJpaRepository.findAll();
        assertThat(savedPriceEntity).hasSize(1)
                .extracting(PriceEntity::getPriceThemeType)
                .containsExactly(FASHION);

        // PriceReferenceImage 검증
        List<PriceReferenceImageEntity> savedPriceRefImageEntity = priceReferenceImageJpaRepository.findAll();
        assertThat(savedPriceRefImageEntity).hasSize(1)
                .extracting(
                        PriceReferenceImageEntity::getFileKey,
                        PriceReferenceImageEntity::getImageOrder
                ).containsExactlyInAnyOrder(
                        tuple("new_file_key", 1)
                );

        // Package 검증
        List<PackageEntity> savedPackageEntity = packageJpaRepository.findAll();
        assertThat(savedPackageEntity).hasSize(1)
                .extracting(
                        PackageEntity::getName,
                        PackageEntity::getPrice,
                        PackageEntity::getContents,
                        PackageEntity::getNotice
                ).containsExactlyInAnyOrder(
                        tuple("new_pkg_name", 10, List.of("new_cnt"), "new_notice")
                );

        // Option 검증
        List<OptionEntity> savedOptions = optionJpaRepository.findAll();
        assertThat(savedOptions).hasSize(1)
                .extracting(
                        OptionEntity::getName,
                        OptionEntity::getCount,
                        OptionEntity::getPrice,
                        OptionEntity::getContents
                ).containsExactlyInAnyOrder(
                        tuple("new_opt_name", 2, 10, List.of("new_cnt"))
                );
    }

    @Test
    @DisplayName("사용자는 가격정보를 수정 할 수 있다.")
    public void applyChanges_edit() throws Exception {
        // given

        // 데이터베이스 데이터 셋팅
        UserEntity userEntity = createUserEntity();

        ExpertEntity expertEntity = createExpertEntity(userEntity);
        userEntity.assignExpertNo(expertEntity.getExpertNo());

        PriceEntity priceEntity = createPriceEntity(expertEntity.getExpertNo(), BEAUTY);

        PackageEntity pkgEntity1 = createPackageEntity(priceEntity, "name", 0, List.of("content"), "notice");
        PackageEntity pkgEntity2 = createPackageEntity(priceEntity, "name", 0, List.of("content"), "notice");

        PriceReferenceImageEntity refImageEntity1 = createReferenceImageEntity(priceEntity, "file_key", 1);
        PriceReferenceImageEntity refImageEntity2 = createReferenceImageEntity(priceEntity, "file_key", 2);

        OptionEntity optEntity1 = createOptionEntity(priceEntity, "name", 0, 0, List.of("content"));
        OptionEntity optEntity2 = createOptionEntity(priceEntity, "name", 0, 0, List.of("content"));

        commitTestTransaction();

        // 요청 값 셋팅
        UpdatePriceReferenceImageWebReq newImgWebReq =
                createPriceRefImageWebRequest(null, "new_file_key", 1, NEW); // 새로 추가
        UpdatePriceReferenceImageWebReq uptImgWebReq =
                createPriceRefImageWebRequest(refImageEntity1.getPriceReferenceImageNo(), refImageEntity1.getFileKey(),
                        2, UPDATE); // 수정
        UpdatePriceReferenceImageWebReq delImgWebReq =
                createPriceRefImageWebRequest(refImageEntity2.getPriceReferenceImageNo(),
                        null, null, DELETE); // 삭제

        UpdatePackageWebReq newPkgWebReq =
                createPackageWebRequest(null, "new_pkg_name", 10, List.of("new_cnt"),
                        "new_notice", NEW); // 추가
        UpdatePackageWebReq uptPkgWebReq =
                createPackageWebRequest(pkgEntity1.getPackageNo(), "changed_pkg_name", 10,
                        List.of("changed_cnt"), "changed_notice", UPDATE); // 수정
        UpdatePackageWebReq delPkgWebReq =
                createPackageWebRequest(pkgEntity2.getPackageNo(), null, null,
                        null, null, DELETE); // 삭제

        UpdateOptionWebReq newOptWebReq =
                createOptionWebRequest(null, "new_opt_name", 2, 10,
                        List.of("new_cnt"), NEW); // 추가
        UpdateOptionWebReq uptOptWebReq =
                createOptionWebRequest(optEntity1.getOptionNo(), "changed_opt_name", 1, 5,
                        List.of("changed_cnt"), UPDATE); // 수정
        UpdateOptionWebReq delOptWebReq =
                createOptionWebRequest(optEntity2.getOptionNo(), null, null, null,
                        null, DELETE); // 삭제

        UpdatePriceWebReq updatePriceWebRequest =
                createPriceWebRequest(priceEntity.getPriceNo(), "FASHION", UPDATE,
                        List.of(newImgWebReq, uptImgWebReq, delImgWebReq),
                        List.of(newPkgWebReq, uptPkgWebReq, delPkgWebReq),
                        List.of(newOptWebReq, uptOptWebReq, delOptWebReq));

        UpdatePriceListWebReq webRequest = new UpdatePriceListWebReq(List.of(updatePriceWebRequest));

        HttpEntity<UpdatePriceListWebReq> request = settingWebRequest(userEntity.getUserNo(), webRequest);

        // when
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/v1/experts/prices",
                HttpMethod.PATCH,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Price 검증
        Optional<PriceEntity> optionalPriceEntity = priceJpaRepository.findById(priceEntity.getPriceNo());
        assertThat(optionalPriceEntity).isPresent();
        PriceEntity updatedPrice = optionalPriceEntity.get();
        assertThat(updatedPrice.getPriceThemeType()).isEqualTo(FASHION);

        // PriceReferenceImage 검증
        List<PriceReferenceImageEntity> updatedImages = priceReferenceImageJpaRepository.findByPriceEntity_PriceNo(priceEntity.getPriceNo());
        assertThat(updatedImages).hasSize(2)
                .extracting(
                        PriceReferenceImageEntity::getFileKey,
                        PriceReferenceImageEntity::getImageOrder
                ).containsExactlyInAnyOrder(
                        tuple("new_file_key", 1),
                        tuple("file_key", 2)
                );

        // Package 검증
        List<PackageEntity> updatedPackages = packageJpaRepository.findByPriceEntity_PriceNo(priceEntity.getPriceNo());
        assertThat(updatedPackages).hasSize(2)
                .extracting(
                        PackageEntity::getName,
                        PackageEntity::getPrice,
                        PackageEntity::getContents,
                        PackageEntity::getNotice
                ).containsExactlyInAnyOrder(
                        tuple("new_pkg_name", 10, List.of("new_cnt"), "new_notice"),
                        tuple("changed_pkg_name", 10, List.of("changed_cnt"), "changed_notice")
                );

        // Option 검증
        List<OptionEntity> updatedOptions = optionJpaRepository.findByPriceEntity_PriceNo(priceEntity.getPriceNo());
        assertThat(updatedOptions).hasSize(2)
                .extracting(
                        OptionEntity::getName,
                        OptionEntity::getCount,
                        OptionEntity::getPrice,
                        OptionEntity::getContents
                ).containsExactlyInAnyOrder(
                        tuple("new_opt_name", 2, 10, List.of("new_cnt")),
                        tuple("changed_opt_name", 1, 5, List.of("changed_cnt"))
                );
    }

    @Test
    @DisplayName("사용자는 가격정보를 삭제 할 수 있다.")
    public void applyChanges_delete() throws Exception {
        // given

        // 데이터베이스 데이터 셋팅
        UserEntity userEntity = createUserEntity();

        ExpertEntity expertEntity = createExpertEntity(userEntity);
        userEntity.assignExpertNo(expertEntity.getExpertNo());

        PriceEntity priceEntity = createPriceEntity(expertEntity.getExpertNo(), BEAUTY);

        createPackageEntity(priceEntity, "name", 0, List.of("content"), "notice");

        createReferenceImageEntity(priceEntity, "file_key", 1);

        createOptionEntity(priceEntity, "name", 0, 0, List.of("content"));

        commitTestTransaction();

        // 요청 값 셋팅

        UpdatePriceWebReq updatePriceWebRequest =
                createPriceWebRequest(priceEntity.getPriceNo(), null, DELETE,
                        null, null, null);

        UpdatePriceListWebReq webRequest = new UpdatePriceListWebReq(List.of(updatePriceWebRequest));

        HttpEntity<UpdatePriceListWebReq> request = settingWebRequest(userEntity.getUserNo(), webRequest);

        // when
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/v1/experts/prices",
                HttpMethod.PATCH,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Price 검증
        Optional<PriceEntity> optionalPriceEntity = priceJpaRepository.findById(priceEntity.getPriceNo());
        assertThat(optionalPriceEntity).isNotPresent();
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
                .expertNo(null)
                .build();
        return userJpaRepository.save(userEntity);
    }

    private ExpertEntity createExpertEntity(UserEntity userEntity) {
        ExpertEntity expertEntity = ExpertEntity.builder()
                .backgroundImageKey("backgroundImageKey")
                .intro("intro")
                .activityCareer("경력 5년")
                .activityAreas(List.of("서울 강북구"))
                .activityCount(0)
                .lastActivityAt(LocalDateTime.of(2020, 10, 10, 1, 0))
                .portfolioLinks(List.of("link"))
                .approvalStatus(ApprovalStatus.PENDING)
                .userEntity(userEntity)
                .build();
        return expertJpaRepository.save(expertEntity);
    }

    private <T> HttpEntity<T> settingWebRequest(String userNo, T webRequest) {
        String accessToken = tokenProvider.createAccessToken(userNo, "ROLE_USER");
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Bearer " + accessToken);
        return new HttpEntity<>(webRequest, headers);
    }

    private void commitTestTransaction() {
        TestTransaction.flagForCommit();  // 지금까지 열린 테스트 트랜잭션을 커밋
        TestTransaction.end(); // 실제 커밋 수행
    }

    private UpdatePriceReferenceImageWebReq createPriceRefImageWebRequest(String priceRefImageNo, String fileKey,
                                                                          Integer imageOrder, ChangeStatus changeStatus) {
        return UpdatePriceReferenceImageWebReq.builder()
                .priceRefImageNo(priceRefImageNo)
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .status(changeStatus)
                .build();
    }

    private UpdatePackageWebReq createPackageWebRequest(String packageNo, String name, Integer price, List<String> contents,
                                                        String notice, ChangeStatus changeStatus) {
        return UpdatePackageWebReq.builder()
                .packageNo(packageNo)
                .name(name)
                .price(price)
                .contents(contents)
                .notice(notice)
                .status(changeStatus)
                .build();
    }

    private UpdateOptionWebReq createOptionWebRequest(String optionNo, String name, Integer count, Integer price,
                                                      List<String> contents, ChangeStatus changeStatus) {
        return UpdateOptionWebReq.builder()
                .optionNo(optionNo)
                .name(name)
                .count(count)
                .price(price)
                .contents(contents)
                .status(changeStatus)
                .build();
    }

    private UpdatePriceWebReq createPriceWebRequest(String priceNo, String theme, ChangeStatus changeStatus,
                                                    List<UpdatePriceReferenceImageWebReq> priceReferenceImages, List<UpdatePackageWebReq> packages, List<UpdateOptionWebReq> options) {
        return UpdatePriceWebReq.builder()
                .priceNo(priceNo)
                .priceThemeType(theme)
                .priceReferenceImages(priceReferenceImages)
                .packages(packages)
                .options(options)
                .status(changeStatus)
                .build();
    }
}
