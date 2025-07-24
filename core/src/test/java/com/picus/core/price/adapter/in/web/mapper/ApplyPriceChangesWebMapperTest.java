package com.picus.core.price.adapter.in.web.mapper;

import com.picus.core.price.adapter.in.web.data.request.*;
import com.picus.core.price.application.port.in.request.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplyPriceChangesWebMapperTest {

    private final ApplyPriceChangesWebMapper mapper = new ApplyPriceChangesWebMapper();

    @Test
    @DisplayName("ApplyPriceChangesWebRequest -> PriceInfoCommandAppReq 매핑")
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
        PriceInfoCommandAppReq command = mapper.toCommand(webRequest);

        // then
        assertThat(command).isNotNull();

        // PriceCommand의 모든 필드 검증
        assertThat(command.prices()).hasSize(1);
        PriceCommandAppReq priceCommandAppReq = command.prices().getFirst();
        assertThat(priceCommandAppReq.priceNo()).isEqualTo("price-001");
        assertThat(priceCommandAppReq.priceThemeType()).isEqualTo("THEME");
        assertThat(priceCommandAppReq.status()).isEqualTo(ChangeStatus.NEW);

        // PriceReferenceImageCommandAppReq 모든 필드 검증
        assertThat(priceCommandAppReq.priceReferenceImages()).hasSize(1);
        PriceReferenceImageCommandAppReq imageCommand = priceCommandAppReq.priceReferenceImages().getFirst();
        assertThat(imageCommand.priceRefImageNo()).isEqualTo("img-001");
        assertThat(imageCommand.fileKey()).isEqualTo("file-key-001");
        assertThat(imageCommand.imageOrder()).isEqualTo(1);
        assertThat(imageCommand.status()).isEqualTo(ChangeStatus.NEW);

        // PackageCommandAppReq 모든 필드 검증
        assertThat(priceCommandAppReq.packages()).hasSize(1);
        PackageCommandAppReq packageCommandAppReq = priceCommandAppReq.packages().getFirst();
        assertThat(packageCommandAppReq.packageNo()).isEqualTo("pkg-001");
        assertThat(packageCommandAppReq.name()).isEqualTo("패키지A");
        assertThat(packageCommandAppReq.price()).isEqualTo(10000);
        assertThat(packageCommandAppReq.contents()).containsExactly("내용1", "내용2");
        assertThat(packageCommandAppReq.notice()).isEqualTo("주의사항");
        assertThat(packageCommandAppReq.status()).isEqualTo(ChangeStatus.NEW);

        // OptionCommandAppReq 모든 필드 검증
        assertThat(priceCommandAppReq.options()).hasSize(1);
        OptionCommandAppReq optionCommandAppReq = priceCommandAppReq.options().getFirst();
        assertThat(optionCommandAppReq.optionNo()).isEqualTo("opt-001");
        assertThat(optionCommandAppReq.name()).isEqualTo("옵션A");
        assertThat(optionCommandAppReq.count()).isEqualTo(2);
        assertThat(optionCommandAppReq.price()).isEqualTo(3000);
        assertThat(optionCommandAppReq.contents()).containsExactly("옵션내용");
        assertThat(optionCommandAppReq.status()).isEqualTo(ChangeStatus.NEW);
    }
}