package com.picus.core.price.adapter.in.web.mapper;

import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.adapter.in.web.data.response.LoadPriceResponse;
import com.picus.core.price.domain.Option;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.PriceReferenceImage;
import com.picus.core.price.domain.vo.SnapSubTheme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class LoadPriceWebMapperTest {
    private final LoadPriceWebMapper mapper = new LoadPriceWebMapper();

    @Test
    @DisplayName("Price 도메인 객체를 WebResponse로 변환한다")
    void toResponse_shouldMapCorrectly() {
        // given
        PriceReferenceImage image = PriceReferenceImage.builder()
                .priceRefImageNo("img_no")
                .fileKey("file-key")
                .imageUrl("https://cdn.picus.com/image.jpg")
                .imageOrder(1)
                .build();

        Package pkg = Package.builder()
                .packageNo("pkg_no")
                .name("기본 패키지")
                .price(100000)
                .contents(List.of("내용1", "내용2"))
                .notice("주의사항")
                .build();

        Option option = Option.builder()
                .optionNo("opt_no")
                .name("옵션A")
                .unitSize(2)
                .pricePerUnit(20000)
                .contents(List.of("옵션내용1", "옵션내용2"))
                .build();

        Price price = Price.builder()
                .priceNo("P123")
                .priceThemeType(PriceThemeType.SNAP)
                .snapSubTheme(SnapSubTheme.ADMISSION)
                .priceReferenceImages(List.of(image))
                .packages(List.of(pkg))
                .options(List.of(option))
                .build();

        // when
        LoadPriceResponse response = mapper.toResponse(List.of(price)); // 리스트로 전달

        // then
        List<LoadPriceResponse.PriceResponse> prices = response.prices();
        assertThat(prices).hasSize(1);

        LoadPriceResponse.PriceResponse first = prices.getFirst();
        assertThat(first.priceNo()).isEqualTo("P123");
        assertThat(first.priceThemeType()).isEqualTo(PriceThemeType.SNAP);
        assertThat(first.snapSubTheme()).isEqualTo(SnapSubTheme.ADMISSION);

        List<LoadPriceResponse.PriceResponse.PriceReferenceImageResponse> imageResponses = first.priceReferenceImages();
        assertThat(imageResponses).hasSize(1)
                .extracting(
                        LoadPriceResponse.PriceResponse.PriceReferenceImageResponse::priceRefImageNo,
                        LoadPriceResponse.PriceResponse.PriceReferenceImageResponse::fileKey,
                        LoadPriceResponse.PriceResponse.PriceReferenceImageResponse::imageUrl,
                        LoadPriceResponse.PriceResponse.PriceReferenceImageResponse::imageOrder
                ).containsExactlyInAnyOrder(
                        tuple("img_no", "file-key", "https://cdn.picus.com/image.jpg", 1)
                );

        List<LoadPriceResponse.PriceResponse.PackageResponse> packageResponses = first.packages();
        assertThat(packageResponses).hasSize(1)
                .extracting(
                        LoadPriceResponse.PriceResponse.PackageResponse::packageNo,
                        LoadPriceResponse.PriceResponse.PackageResponse::name,
                        LoadPriceResponse.PriceResponse.PackageResponse::price,
                        LoadPriceResponse.PriceResponse.PackageResponse::contents,
                        LoadPriceResponse.PriceResponse.PackageResponse::notice
                ).containsExactlyInAnyOrder(
                        tuple("pkg_no", "기본 패키지", 100000, List.of("내용1", "내용2"), "주의사항")
                );

        List<LoadPriceResponse.PriceResponse.OptionResponse> optionResponses = first.options();
        assertThat(optionResponses).hasSize(1)
                .extracting(
                        LoadPriceResponse.PriceResponse.OptionResponse::optionNo,
                        LoadPriceResponse.PriceResponse.OptionResponse::name,
                        LoadPriceResponse.PriceResponse.OptionResponse::count,
                        LoadPriceResponse.PriceResponse.OptionResponse::price,
                        LoadPriceResponse.PriceResponse.OptionResponse::contents
                ).containsExactlyInAnyOrder(
                        tuple("opt_no", "옵션A", 2, 20000, List.of("옵션내용1", "옵션내용2"))
                );
    }
}