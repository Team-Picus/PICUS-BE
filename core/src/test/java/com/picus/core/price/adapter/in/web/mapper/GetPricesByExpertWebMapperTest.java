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

class GetPricesByExpertWebMapperTest {
    private final GetPricesByExpertWebMapper mapper = new GetPricesByExpertWebMapper();

    @Test
    @DisplayName("Price 도메인 객체를 WebResponse로 변환한다")
    void toWebResponse_shouldMapCorrectly() {
        // given
        PriceReferenceImage image = PriceReferenceImage.builder()
                .fileKey("file-key")
                .imageUrl("https://cdn.picus.com/image.jpg")
                .imageOrder(1)
                .build();

        Package pkg = Package.builder()
                .name("기본 패키지")
                .price(100000)
                .contents(List.of("내용1", "내용2"))
                .notice("주의사항")
                .build();

        Option option = Option.builder()
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

        assertThat(response.priceReferenceImages()).hasSize(1);
        PriceReferenceImageWebResponse imageResponse = response.priceReferenceImages().getFirst();
        assertThat(imageResponse.imageUrl()).isEqualTo("https://cdn.picus.com/image.jpg");
        assertThat(imageResponse.imageOrder()).isEqualTo(1);

        assertThat(response.packages()).hasSize(1);
        PackageWebResponse pkgResponse = response.packages().getFirst();
        assertThat(pkgResponse.name()).isEqualTo("기본 패키지");
        assertThat(pkgResponse.price()).isEqualTo(100000);
        assertThat(pkgResponse.contents()).containsExactly("내용1", "내용2");
        assertThat(pkgResponse.notice()).isEqualTo("주의사항");

        assertThat(response.options()).hasSize(1);
        OptionWebResponse optionResponse = response.options().getFirst();
        assertThat(optionResponse.name()).isEqualTo("옵션A");
        assertThat(optionResponse.count()).isEqualTo(2);
        assertThat(optionResponse.price()).isEqualTo(20000);
        assertThat(optionResponse.contents()).containsExactly("옵션내용1", "옵션내용2");
    }
}