package com.picus.core.price.adapter.out.persistence;

import com.picus.core.expert.domain.model.vo.PriceThemeType;
import com.picus.core.price.adapter.out.persistence.entity.OptionEntity;
import com.picus.core.price.adapter.out.persistence.entity.PackageEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceReferenceImageEntity;
import com.picus.core.price.adapter.out.persistence.mapper.OptionPersistenceMapper;
import com.picus.core.price.adapter.out.persistence.mapper.PackagePersistenceMapper;
import com.picus.core.price.adapter.out.persistence.mapper.PriceQueryPersistenceAdapter;
import com.picus.core.price.adapter.out.persistence.mapper.PricePersistenceMapper;
import com.picus.core.price.adapter.out.persistence.repository.OptionJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PackageJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceJpaRepository;
import com.picus.core.price.adapter.out.persistence.repository.PriceReferenceImageJpaRepository;
import com.picus.core.price.domain.model.Option;
import com.picus.core.price.domain.model.Package;
import com.picus.core.price.domain.model.Price;
import com.picus.core.price.domain.model.PriceReferenceImage;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({
        PriceQueryPersistenceAdapter.class,
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
    PriceQueryPersistenceAdapter pricePersistenceAdapter;
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
                .content(content)
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

    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }

}