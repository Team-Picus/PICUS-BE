package com.picus.core.price.adapter.in.web.mapper;

import com.picus.core.price.adapter.in.web.data.response.LoadMyPackageResponse;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.domain.vo.SnapSubTheme;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class LoadMyPackageWebMapperTest {

    private final LoadMyPackageWebMapper webMapper = new LoadMyPackageWebMapper();

    @Test
    @DisplayName("List<Price> -> LoadMyPackageResponse")
    public void toResponse() throws Exception {
        // given
        List<Price> prices = createPrices(PriceThemeType.SNAP, SnapSubTheme.ADMISSION,
                List.of(Package.builder()
                        .packageNo("pkg-123")
                        .name("pkg_name1")
                        .build()));
        // when
        LoadMyPackageResponse response = webMapper.toResponse(prices);

        // then
        assertThat(response.prices()).hasSize(1)
                .extracting(
                        LoadMyPackageResponse.PriceResponse::priceThemeType,
                        LoadMyPackageResponse.PriceResponse::snapSubTheme
                ).containsExactly(
                        Tuple.tuple(PriceThemeType.SNAP, SnapSubTheme.ADMISSION)
                );
        assertThat(response.prices().getFirst().packages()).hasSize(1)
                .extracting(
                        LoadMyPackageResponse.PriceResponse.PackageResponse::packageNo,
                        LoadMyPackageResponse.PriceResponse.PackageResponse::name
                ).containsExactly(
                        Tuple.tuple("pkg-123", "pkg_name1")
                );
    }

    private List<Price> createPrices(PriceThemeType priceThemeType, SnapSubTheme snapSubTheme, List<Package> packages) {
        return List.of(
                Price.builder()
                        .priceThemeType(priceThemeType)
                        .snapSubTheme(snapSubTheme)
                        .packages(packages)
                        .build()
        );
    }

}