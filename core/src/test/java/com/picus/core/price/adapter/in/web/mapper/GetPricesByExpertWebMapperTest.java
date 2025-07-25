package com.picus.core.price.adapter.in.web.mapper;

import com.picus.core.expert.domain.model.vo.PriceThemeType;
import com.picus.core.price.adapter.in.web.data.response.GetPricesByExpertWebResponse;
import com.picus.core.price.adapter.in.web.data.response.GetPricesByExpertWebResponse.PriceReferenceImageWebResponse;
import com.picus.core.price.domain.model.Option;
import com.picus.core.price.domain.model.Package;
import com.picus.core.price.domain.model.Price;
import com.picus.core.price.domain.model.PriceReferenceImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.picus.core.price.adapter.in.web.data.response.GetPricesByExpertWebResponse.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class GetPricesByExpertWebMapperTest {
    private final GetPricesByExpertWebMapper mapper = new GetPricesByExpertWebMapper();

    @Test
    @DisplayName("Price 도메인 객체를 WebResponse로 변환한다")
    void toWebResponse_shouldMapCorrectly() {
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
                .count(2)
                .price(20000)
                .contents(List.of("옵션내용1", "옵션내용2"))
                .build();

        Price price = Price.builder()
                .priceNo("P123")
                .priceThemeType(PriceThemeType.BEAUTY)
                .priceReferenceImages(List.of(image))
                .packages(List.of(pkg))
                .options(List.of(option))
                .build();

        // when
        GetPricesByExpertWebResponse response = mapper.toWebResponse(price);

        // then
        assertThat(response.priceNo()).isEqualTo("P123");
        assertThat(response.priceThemeType()).isEqualTo("BEAUTY");

        List<PriceReferenceImageWebResponse> imageResponses = response.priceReferenceImages();
        assertThat(imageResponses).hasSize(1)
                .extracting(
                        PriceReferenceImageWebResponse::priceRefImageNo,
                        PriceReferenceImageWebResponse::imageUrl,
                        PriceReferenceImageWebResponse::imageOrder
                ).containsExactlyInAnyOrder(
                        tuple("img_no", "https://cdn.picus.com/image.jpg", 1)
                );

        List<PackageWebResponse> packageResponses = response.packages();
        assertThat(packageResponses).hasSize(1)
                .extracting(
                        PackageWebResponse::packageNo,
                        PackageWebResponse::name,
                        PackageWebResponse::price,
                        PackageWebResponse::contents,
                        PackageWebResponse::notice
                ).containsExactlyInAnyOrder(
                        tuple("pkg_no", "기본 패키지", 100000, List.of("내용1", "내용2"), "주의사항")
                );

        List<OptionWebResponse> optionResponses = response.options();
        assertThat(optionResponses).hasSize(1)
                .extracting(
                        OptionWebResponse::optionNo,
                        OptionWebResponse::name,
                        OptionWebResponse::count,
                        OptionWebResponse::price,
                        OptionWebResponse::contents
                ).containsExactlyInAnyOrder(
                        tuple("opt_no", "옵션A", 2, 20000, List.of("옵션내용1", "옵션내용2"))
                );
    }
}