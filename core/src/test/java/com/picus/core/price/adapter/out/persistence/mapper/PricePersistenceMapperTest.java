package com.picus.core.price.adapter.out.persistence.mapper;

import com.picus.core.expert.domain.model.vo.PriceThemeType;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import com.picus.core.price.domain.model.Option;
import com.picus.core.price.domain.model.Package;
import com.picus.core.price.domain.model.Price;
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

        List<Package> packages = createPackages(testPriceNo);

        List<Option> options = createOptions(testPriceNo);

        // when
        Price result = mapper.toDomain(priceEntity, packages, options);

        // then
        assertThat(result.getPriceNo()).isEqualTo(testPriceNo);
        assertThat(result.getPriceThemeType()).isEqualTo(testPriceThemeType);
        assertThat(result.getPackages()).isEqualTo(packages);
        assertThat(result.getOptions()).isEqualTo(options);
    }

    private PriceEntity createPriceEntity(String testPriceNo, PriceThemeType testPriceThemeType) {
        return PriceEntity.builder()
                .priceNo(testPriceNo)
                .expertNo("expert001")
                .priceThemeType(testPriceThemeType)
                .build();
    }

    private List<Package> createPackages(String testPriceNo) {
        return List.of(
                Package.builder()
                        .packageNo("pkg1")
                        .priceNo(testPriceNo)
                        .name("기본 패키지")
                        .price(10000)
                        .contents(List.of("내용1"))
                        .notice("주의사항")
                        .build()
        );
    }

    private List<Option> createOptions(String testPriceNo) {
        return List.of(
                Option.builder()
                        .optionNo("opt1")
                        .priceNo(testPriceNo)
                        .name("옵션1")
                        .count(1)
                        .price(1000)
                        .content(List.of("추가1"))
                        .build()
        );
    }
}