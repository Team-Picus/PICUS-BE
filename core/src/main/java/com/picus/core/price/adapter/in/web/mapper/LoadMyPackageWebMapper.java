package com.picus.core.price.adapter.in.web.mapper;

import com.picus.core.price.adapter.in.web.data.response.LoadMyPackageResponse;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoadMyPackageWebMapper {

    public LoadMyPackageResponse toResponse(List<Price> prices) {
        return LoadMyPackageResponse.builder()
                .prices(toPriceResponse(prices))
                .build();
    }

    private List<LoadMyPackageResponse.PriceResponse> toPriceResponse(List<Price> prices) {
        return prices.stream()
                .map(
                        price -> LoadMyPackageResponse.PriceResponse.builder()
                                .priceThemeType(price.getPriceThemeType())
                                .snapSubTheme(price.getSnapSubTheme())
                                .packages(toPackageResponse(price.getPackages()))
                                .build()
                ).toList();
    }

    private List<LoadMyPackageResponse.PriceResponse.PackageResponse> toPackageResponse(List<Package> packages) {
        return packages.stream()
                .map(pkg -> LoadMyPackageResponse.PriceResponse.PackageResponse.builder()
                        .packageNo(pkg.getPackageNo())
                        .name(pkg.getName())
                        .build()
                ).toList();
    }
}
