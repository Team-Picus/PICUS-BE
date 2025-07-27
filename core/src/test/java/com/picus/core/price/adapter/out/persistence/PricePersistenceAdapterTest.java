package com.picus.core.price.adapter.out.persistence;

import com.picus.core.expert.domain.vo.PriceThemeType;
import com.picus.core.price.adapter.out.persistence.entity.OptionEntity;
import com.picus.core.price.adapter.out.persistence.entity.PackageEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceReferenceImageEntity;
import com.picus.core.price.adapter.out.persistence.mapper.OptionPersistenceMapper;
import com.picus.core.price.adapter.out.persistence.mapper.PackagePersistenceMapper;
import com.picus.core.price.adapter.out.persistence.mapper.PricePersistenceMapper;
import com.picus.core.price.adapter.out.persistence.mapper.PriceReferenceImagePersistenceMapper;
import com.picus.core.price.adapter.out.persistence.repository.OptionJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PackageJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceReferenceImageJpaRepository;
import com.picus.core.price.domain.Option;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.PriceReferenceImage;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.picus.core.expert.domain.vo.PriceThemeType.FASHION;
import static org.assertj.core.api.Assertions.*;

@Import({
        PricePersistenceAdapter.class,
        PricePersistenceMapper.class,
        PackagePersistenceMapper.class,
        OptionPersistenceMapper.class,
        PriceReferenceImagePersistenceMapper.class
})
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PricePersistenceAdapterTest {

    @Autowired
    PricePersistenceAdapter pricePersistenceAdapter;
    @Autowired
    PriceJpaRepository priceJpaRepository;
    @Autowired
    PackageJpaRepository packageJpaRepository;
    @Autowired
    OptionJpaRepository optionJpaRepository;
    @Autowired
    PriceReferenceImageJpaRepository priceReferenceImageJpaRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("특정 expertNo을 가진 Price를 조회한다.")
    public void findByExpertNo_success() {
        // given
        PriceEntity priceEntity = createPriceEntity("expert001", PriceThemeType.BEAUTY);
        PackageEntity pkgEntity = createPackageEntity(priceEntity, "기본 패키지", 20000, List.of("헤어컷", "드라이"), "사전 예약 필수");
        OptionEntity optEntity = createOptionEntity(priceEntity, "옵션 A", 1, 5000, List.of("마사지 추가"));
        PriceReferenceImageEntity imgEntity = createReferenceImageEntity(priceEntity, "file-key-123", 1);

        clearPersistenceContext();

        // when
        List<Price> results = pricePersistenceAdapter.findByExpertNo("expert001");

        // then
        assertThat(results).hasSize(1);
        Price result = results.getFirst();

        // Price 필드 검증
        assertThat(result.getPriceThemeType()).isEqualTo(PriceThemeType.BEAUTY);

        // Package 검증
        assertThat(result.getPackages()).hasSize(1);
        Package pkg = result.getPackages().getFirst();
        assertThat(pkg.getName()).isEqualTo("기본 패키지");
        assertThat(pkg.getPrice()).isEqualTo(20000);
        assertThat(pkg.getContents()).isEqualTo(List.of("헤어컷", "드라이"));
        assertThat(pkg.getNotice()).isEqualTo("사전 예약 필수");

        // Option 검증
        assertThat(result.getOptions()).hasSize(1);
        Option opt = result.getOptions().getFirst();
        assertThat(opt.getName()).isEqualTo("옵션 A");
        assertThat(opt.getCount()).isEqualTo(1);
        assertThat(opt.getPrice()).isEqualTo(5000);
        assertThat(opt.getContents()).isEqualTo(List.of("마사지 추가"));

        // PriceReferenceImage 검증
        assertThat(result.getPriceReferenceImages()).hasSize(1);
        PriceReferenceImage referenceImg = result.getPriceReferenceImages().getFirst();
        assertThat(referenceImg.getFileKey()).isEqualTo("file-key-123");
        assertThat(referenceImg.getImageOrder()).isEqualTo(1);
    }

    @Test
    @DisplayName("특정 priceNo를 가진 Price를 조회한다.")
    public void findById_success() {
        // given
        PriceEntity priceEntity = createPriceEntity("expert001", PriceThemeType.BEAUTY);
        createPackageEntity(priceEntity, "기본 패키지", 20000, List.of("헤어컷", "드라이"), "사전 예약 필수");
        createOptionEntity(priceEntity, "옵션 A", 1, 5000, List.of("마사지 추가"));
        createReferenceImageEntity(priceEntity, "file-key-123", 1);

        clearPersistenceContext();

        // when
        Price result = pricePersistenceAdapter.findById(priceEntity.getPriceNo());

        // then

        // Price 필드 검증
        assertThat(result.getPriceThemeType()).isEqualTo(PriceThemeType.BEAUTY);

        // Package 검증
        assertThat(result.getPackages()).hasSize(1);
        Package pkg = result.getPackages().getFirst();
        assertThat(pkg.getName()).isEqualTo("기본 패키지");
        assertThat(pkg.getPrice()).isEqualTo(20000);
        assertThat(pkg.getContents()).isEqualTo(List.of("헤어컷", "드라이"));
        assertThat(pkg.getNotice()).isEqualTo("사전 예약 필수");

        // Option 검증
        assertThat(result.getOptions()).hasSize(1);
        Option opt = result.getOptions().getFirst();
        assertThat(opt.getName()).isEqualTo("옵션 A");
        assertThat(opt.getCount()).isEqualTo(1);
        assertThat(opt.getPrice()).isEqualTo(5000);
        assertThat(opt.getContents()).isEqualTo(List.of("마사지 추가"));

        // PriceReferenceImage 검증
        assertThat(result.getPriceReferenceImages()).hasSize(1);
        PriceReferenceImage referenceImg = result.getPriceReferenceImages().getFirst();
        assertThat(referenceImg.getFileKey()).isEqualTo("file-key-123");
        assertThat(referenceImg.getImageOrder()).isEqualTo(1);
    }

    @Test
    @DisplayName("Price와 그 Price의 Package, Option, PriceRefImage들을 저장한다.")
    public void save() throws Exception {
        // given
        Price price = createPriceDomain(
                FASHION,
                List.of(
                        createPriceReferenceImage(null, "fileKey1", 1),
                        createPriceReferenceImage(null, "fileKey2", 2)
                ),
                List.of(
                        createPackage(null, "패키지1", 1000, List.of("A", "B"), "노티스1"),
                        createPackage(null, "패키지2", 2000, List.of("C"), "노티스2")
                ),
                List.of(
                        createOption(null, "옵션1", 1, 100, List.of("X")),
                        createOption(null, "옵션2", 2, 200, List.of("Y"))
                )
        );
        String expertNo = "expert_no";

        // when
        Price saved = pricePersistenceAdapter.save(price, expertNo);

        // then
        // Price
        assertThat(saved.getPriceNo()).isNotNull();
        assertThat(saved.getPriceThemeType()).isEqualTo(FASHION);

        // PriceRefImage
        assertThat(saved.getPriceReferenceImages())
                .hasSize(2)
                .satisfiesExactlyInAnyOrder(
                        image -> {
                            assertThat(image.getPriceRefImageNo()).isNotNull();
                            assertThat(image.getFileKey()).isEqualTo("fileKey1");
                            assertThat(image.getImageOrder()).isEqualTo(1);
                        },
                        image -> {
                            assertThat(image.getPriceRefImageNo()).isNotNull();
                            assertThat(image.getFileKey()).isEqualTo("fileKey2");
                            assertThat(image.getImageOrder()).isEqualTo(2);
                        }
                );

        // Package
        assertThat(saved.getPackages())
                .hasSize(2)
                .satisfiesExactlyInAnyOrder(
                        p -> {
                            assertThat(p.getPackageNo()).isNotNull();
                            assertThat(p.getName()).isEqualTo("패키지1");
                            assertThat(p.getPrice()).isEqualTo(1000);
                            assertThat(p.getContents()).isEqualTo(List.of("A", "B"));
                            assertThat(p.getNotice()).isEqualTo("노티스1");
                        },
                        p -> {
                            assertThat(p.getPackageNo()).isNotNull();
                            assertThat(p.getName()).isEqualTo("패키지2");
                            assertThat(p.getPrice()).isEqualTo(2000);
                            assertThat(p.getContents()).isEqualTo(List.of("C"));
                            assertThat(p.getNotice()).isEqualTo("노티스2");
                        }
                );

        // Option
        assertThat(saved.getOptions())
                .hasSize(2)
                .satisfiesExactlyInAnyOrder(
                        o -> {
                            assertThat(o.getOptionNo()).isNotNull();
                            assertThat(o.getName()).isEqualTo("옵션1");
                            assertThat(o.getCount()).isEqualTo(1);
                            assertThat(o.getPrice()).isEqualTo(100);
                            assertThat(o.getContents()).isEqualTo(List.of("X"));
                        },
                        o -> {
                            assertThat(o.getOptionNo()).isNotNull();
                            assertThat(o.getName()).isEqualTo("옵션2");
                            assertThat(o.getCount()).isEqualTo(2);
                            assertThat(o.getPrice()).isEqualTo(200);
                            assertThat(o.getContents()).isEqualTo(List.of("Y"));
                        }
                );
    }

    @Test
    @DisplayName("Price, PriceRefImage, Package, Option을 삭제한다.")
    public void delete() throws Exception {
        // given
        PriceEntity priceEntity = createPriceEntity("expert_no", PriceThemeType.BEAUTY);
        PackageEntity packageEntity = createPackageEntity(priceEntity, "name", 0, List.of("content"), "notice");
        OptionEntity optionEntity = createOptionEntity(priceEntity, "name", 0, 0, List.of("content"));
        PriceReferenceImageEntity referenceImageEntity = createReferenceImageEntity(priceEntity, "file_key", 1);

        clearPersistenceContext();

        // when
        pricePersistenceAdapter.delete(priceEntity.getPriceNo());

        // then
        assertThat(priceJpaRepository.findById(priceEntity.getPriceNo())).isNotPresent();
        assertThat(packageJpaRepository.findById(packageEntity.getPackageNo())).isNotPresent();
        assertThat(optionJpaRepository.findById(optionEntity.getOptionNo())).isNotPresent();
        assertThat(priceReferenceImageJpaRepository.findById(referenceImageEntity.getPriceReferenceImageNo())).isNotPresent();

    }

    @Test
    @DisplayName("Price를 수정한다. 이때 PriceRefImage, Package, Option은 경우에 따라 추가/수정/삭제 된다.")
    public void update() throws Exception {
        // given

        // 데이터베이스에 데이터 셋팅
        PriceEntity priceEntity = createPriceEntity("expert_no", PriceThemeType.BEAUTY);
        String priceNo = priceEntity.getPriceNo();

        PackageEntity packageEntity1 = createPackageEntity(priceEntity, "name", 0, List.of("content"), "notice");
        PackageEntity packageEntity2 = createPackageEntity(priceEntity, "name", 0, List.of("content"), "notice");

        PriceReferenceImageEntity referenceImageEntity1 = createReferenceImageEntity(priceEntity, "file_key", 1);
        PriceReferenceImageEntity referenceImageEntity2 = createReferenceImageEntity(priceEntity, "file_key", 2);

        OptionEntity optionEntity1 = createOptionEntity(priceEntity, "name", 0, 0, List.of("content"));
        OptionEntity optionEntity2 = createOptionEntity(priceEntity, "name", 0, 0, List.of("content"));

        clearPersistenceContext();

        // 수정할 Price 객체
        Price updatedPrice = Price.builder()
                .priceNo(priceNo)
                .priceThemeType(FASHION)
                .priceReferenceImages(List.of(
                        createPriceReferenceImage(null, "new_file_key", 1), // 추가된 PriceReferenceImage
                        createPriceReferenceImage(referenceImageEntity1.getPriceReferenceImageNo(), "file_key", 2) // 수정된 PriceReferenceImage. file_key자체는 수정이 불가능함
                ))
                .packages(List.of(
                        createPackage(null, "new_pkg_name", 10, List.of("new_cnt"), "new_notice"), // 추가된 Package
                        createPackage(packageEntity1.getPackageNo(), "changed_pkg_name", 10, List.of("changed_cnt"), "changed_notice") // 수정된 Package
                ))
                .options(List.of(
                        createOption(null, "new_opt_name", 2, 10, List.of("new_cnt")), // 추가된 Option
                        createOption(optionEntity1.getOptionNo(), "changed_opt_name", 1, 5, List.of("changed_cnt")) // 수정된 Option
                ))
                .build();

        // 삭제할 No들
        List<String> deletedImageNos = List.of(referenceImageEntity2.getPriceReferenceImageNo());
        List<String> deletedPackageNos = List.of(packageEntity2.getPackageNo());
        List<String> deletedOptionNos = List.of(optionEntity2.getOptionNo());

        // when
        pricePersistenceAdapter.update(updatedPrice, deletedImageNos, deletedPackageNos, deletedOptionNos);
        clearPersistenceContext();

        // then

        // PriceEntity 검증
        PriceEntity priceResult = priceJpaRepository.findById(priceNo)
                .orElseThrow();
        assertThat(priceResult.getPriceThemeType()).isEqualTo(FASHION);

        // PriceReferenceImageEntity 검증
        List<PriceReferenceImageEntity> imageResults = priceReferenceImageJpaRepository.findByPriceEntity_PriceNo(priceNo);
        assertThat(imageResults).hasSize(2)
                .extracting(
                        PriceReferenceImageEntity::getFileKey,
                        PriceReferenceImageEntity::getImageOrder
                ).containsExactlyInAnyOrder(
                        tuple("new_file_key", 1),
                        tuple("file_key", 2)
                );

        // PackageEntity 검증
        List<PackageEntity> packageResults = packageJpaRepository.findByPriceEntity_PriceNo(priceNo);
        assertThat(packageResults).hasSize(2)
                .extracting(
                        PackageEntity::getName,
                        PackageEntity::getPrice,
                        PackageEntity::getContents,
                        PackageEntity::getNotice
                ).containsExactlyInAnyOrder(
                        tuple("new_pkg_name", 10, List.of("new_cnt"), "new_notice"),
                        tuple("changed_pkg_name", 10, List.of("changed_cnt"), "changed_notice")
                );

        // OptionEntity 검증
        List<OptionEntity> optionResults = optionJpaRepository.findByPriceEntity_PriceNo(priceNo);
        assertThat(optionResults).hasSize(2)
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


    /**
     * private 메서드
     */
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


    private Price createPriceDomain(PriceThemeType priceThemeType,
                                    List<PriceReferenceImage> priceReferenceImages,
                                    List<Package> packages,
                                    List<Option> options) {
        return Price.builder()
                .priceThemeType(priceThemeType)
                .priceReferenceImages(priceReferenceImages)
                .packages(packages)
                .options(options)
                .build();
    }

    private PriceReferenceImage createPriceReferenceImage(String imageNo, String fileKey, int imageOrder) {
        return PriceReferenceImage.builder()
                .priceRefImageNo(imageNo)
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .build();
    }

    private Package createPackage(String packageNo, String name, int price, List<String> contents, String notice) {
        return Package.builder()
                .packageNo(packageNo)
                .name(name)
                .price(price)
                .contents(contents)
                .notice(notice)
                .build();
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

    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }

}