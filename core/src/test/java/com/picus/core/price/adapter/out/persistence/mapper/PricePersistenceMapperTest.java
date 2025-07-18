package com.picus.core.price.adapter.out.persistence.mapper;

import com.picus.core.expert.domain.model.vo.PriceThemeType;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import com.picus.core.price.domain.model.Option;
import com.picus.core.price.domain.model.Package;
import com.picus.core.price.domain.model.Price;
import com.picus.core.price.domain.model.PriceReferenceImage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PricePersistenceMapperTest {

    private final PricePersistenceMapper mapper = new PricePersistenceMapper();

    @Test
    void toDomain_shouldMapCorrectly() {
        // given
        String testPriceNo = "price123";
        PriceThemeType testPriceThemeType = PriceThemeType.BEAUTY;

        PriceEntity priceEntity = createPriceEntity(testPriceNo, testPriceThemeType);

        List<Package> packages = createPackages();

        List<Option> options = createOptions();

        List<PriceReferenceImage> referenceImages = createReferenceImages();

        // when
        Price result = mapper.toDomain(priceEntity, packages, options, referenceImages);

        // then
        assertThat(result.getPriceNo()).isEqualTo(testPriceNo);
        assertThat(result.getPriceThemeType()).isEqualTo(testPriceThemeType);
        assertThat(result.getPackages()).isEqualTo(packages);
        assertThat(result.getOptions()).isEqualTo(options);
        assertThat(result.getPriceReferenceImages()).isEqualTo(referenceImages);
    }

    private PriceEntity createPriceEntity(String testPriceNo, PriceThemeType testPriceThemeType) {
        return PriceEntity.builder()
                .priceNo(testPriceNo)
                .expertNo("expert001")
                .priceThemeType(testPriceThemeType)
                .build();
    }

    private List<Package> createPackages() {
        return List.of(
                Package.builder()
                        .name("기본 패키지")
                        .price(10000)
                        .contents(List.of("내용1"))
                        .notice("주의사항")
                        .build()
        );
    }

    private List<Option> createOptions() {
        return List.of(
                Option.builder()
                        .name("옵션1")
                        .count(1)
                        .price(1000)
                        .content(List.of("추가1"))
                        .build()
        );
    }

    private List<PriceReferenceImage> createReferenceImages() {
        return List.of(
                PriceReferenceImage.builder()
                        .fileKey("file-key-123")
                        .imageOrder(1)
                        .build()
        );
    }
}