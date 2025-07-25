package com.picus.core.price.adapter.in.web.mapper;

import com.picus.core.price.adapter.in.web.data.request.*;
import com.picus.core.price.application.port.in.command.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplyPriceChangesWebMapperTest {

    private final ApplyPriceChangesWebMapper mapper = new ApplyPriceChangesWebMapper();

    @Test
    @DisplayName("ApplyPriceChangesWebRequest -> ApplyPriceChangesCommand 매핑")
    void toCommand() {
        // given
        PriceReferenceImageWebRequest imageWebRequest = PriceReferenceImageWebRequest.builder()
                .priceRefImageNo("img-001")
                .fileKey("file-key-001")
                .imageOrder(1)
                .status(ChangeStatus.NEW)
                .build();

        PackageWebRequest packageWebRequest = PackageWebRequest.builder()
                .packageNo("pkg-001")
                .name("패키지A")
                .price(10000)
                .contents(List.of("내용1", "내용2"))
                .notice("주의사항")
                .status(ChangeStatus.NEW)
                .build();

        OptionWebRequest optionWebRequest = OptionWebRequest.builder()
                .optionNo("opt-001")
                .name("옵션A")
                .count(2)
                .price(3000)
                .contents(List.of("옵션내용"))
                .status(ChangeStatus.NEW)
                .build();

        PriceWebRequest priceWebRequest = PriceWebRequest.builder()
                .priceNo("price-001")
                .priceThemeType("THEME")
                .priceReferenceImages(List.of(imageWebRequest))
                .packages(List.of(packageWebRequest))
                .options(List.of(optionWebRequest))
                .status(ChangeStatus.NEW)
                .build();

        ApplyPriceChangesWebRequest webRequest = new ApplyPriceChangesWebRequest(List.of(priceWebRequest));

        // when
        ApplyPriceChangesCommand command = mapper.toCommand(webRequest);

        // then
        assertThat(command).isNotNull();

        // PriceCommand의 모든 필드 검증
        assertThat(command.prices()).hasSize(1);
        PriceCommand priceCommand = command.prices().getFirst();
        assertThat(priceCommand.priceNo()).isEqualTo("price-001");
        assertThat(priceCommand.priceThemeType()).isEqualTo("THEME");
        assertThat(priceCommand.status()).isEqualTo(ChangeStatus.NEW);

        // PriceReferenceImageCommand 모든 필드 검증
        assertThat(priceCommand.priceReferenceImages()).hasSize(1);
        PriceReferenceImageCommand imageCommand = priceCommand.priceReferenceImages().getFirst();
        assertThat(imageCommand.priceRefImageNo()).isEqualTo("img-001");
        assertThat(imageCommand.fileKey()).isEqualTo("file-key-001");
        assertThat(imageCommand.imageOrder()).isEqualTo(1);
        assertThat(imageCommand.status()).isEqualTo(ChangeStatus.NEW);

        // PackageCommand 모든 필드 검증
        assertThat(priceCommand.packages()).hasSize(1);
        PackageCommand packageCommand = priceCommand.packages().getFirst();
        assertThat(packageCommand.packageNo()).isEqualTo("pkg-001");
        assertThat(packageCommand.name()).isEqualTo("패키지A");
        assertThat(packageCommand.price()).isEqualTo(10000);
        assertThat(packageCommand.contents()).containsExactly("내용1", "내용2");
        assertThat(packageCommand.notice()).isEqualTo("주의사항");
        assertThat(packageCommand.status()).isEqualTo(ChangeStatus.NEW);

        // OptionCommand 모든 필드 검증
        assertThat(priceCommand.options()).hasSize(1);
        OptionCommand optionCommand = priceCommand.options().getFirst();
        assertThat(optionCommand.optionNo()).isEqualTo("opt-001");
        assertThat(optionCommand.name()).isEqualTo("옵션A");
        assertThat(optionCommand.count()).isEqualTo(2);
        assertThat(optionCommand.price()).isEqualTo(3000);
        assertThat(optionCommand.contents()).containsExactly("옵션내용");
        assertThat(optionCommand.status()).isEqualTo(ChangeStatus.NEW);
    }
}