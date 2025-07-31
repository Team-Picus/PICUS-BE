package com.picus.core.price.adapter.out.persistence.mapper;

import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import com.picus.core.price.domain.Option;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.PriceReferenceImage;
import com.picus.core.price.domain.vo.SnapSubTheme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PricePersistenceMapperTest {

    private final PricePersistenceMapper mapper = new PricePersistenceMapper();

    @Test
    @DisplayName("PriceEntity -> Price 매핑")
    void toDomain_shouldMapCorrectly() {
        // given
        String testPriceNo = "price123";
        String testExpertNo = "expert001";
        PriceThemeType testPriceThemeType = PriceThemeType.SNAP;
        SnapSubTheme testSnapSubTheme = SnapSubTheme.ADMISSION;

        PriceEntity priceEntity = createPriceEntity(testPriceNo, testExpertNo, testPriceThemeType, testSnapSubTheme);

        List<Package> packages = createPackages();
        List<Option> options = createOptions();
        List<PriceReferenceImage> referenceImages = createReferenceImages();

        // when
        Price result = mapper.toDomain(priceEntity, packages, options, referenceImages);

        // then
        assertThat(result.getPriceNo()).isEqualTo(testPriceNo);
        assertThat(result.getExpertNo()).isEqualTo(testExpertNo);
        assertThat(result.getPriceThemeType()).isEqualTo(testPriceThemeType);
        assertThat(result.getSnapSubTheme()).isEqualTo(testSnapSubTheme);
        assertThat(result.getPackages()).isEqualTo(packages);
        assertThat(result.getOptions()).isEqualTo(options);
        assertThat(result.getPriceReferenceImages()).isEqualTo(referenceImages);
    }

    @Test
    @DisplayName("Price -> PriceEntity 매핑")
    void toEntity_shouldMapCorrectly() {
        // given
        Price price = Price.builder()
                .priceThemeType(PriceThemeType.SNAP)
                .snapSubTheme(SnapSubTheme.ADMISSION)
                .build();
        String expertNo = "expert-123";

        // when
        PriceEntity entity = mapper.toEntity(price, expertNo);

        // then
        assertThat(entity.getExpertNo()).isEqualTo("expert-123");
        assertThat(entity.getPriceThemeType()).isEqualTo(price.getPriceThemeType());
        assertThat(entity.getSnapSubTheme()).isEqualTo(price.getSnapSubTheme());
    }

    private PriceEntity createPriceEntity(String testPriceNo, String testExpertNo, PriceThemeType testPriceThemeType, SnapSubTheme snapSubTheme) {
        return PriceEntity.builder()
                .priceNo(testPriceNo)
                .expertNo(testExpertNo)
                .priceThemeType(testPriceThemeType)
                .snapSubTheme(snapSubTheme)
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
                        .contents(List.of("추가1"))
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