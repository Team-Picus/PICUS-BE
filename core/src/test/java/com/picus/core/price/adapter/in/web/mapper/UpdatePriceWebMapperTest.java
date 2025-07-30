package com.picus.core.price.adapter.in.web.mapper;

import com.picus.core.price.adapter.in.web.data.request.*;
import com.picus.core.price.application.port.in.command.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdatePriceWebMapperTest {

    private final UpdatePriceWebMapper mapper = new UpdatePriceWebMapper();

    @Test
    @DisplayName("UpdatePriceListRequest -> UpdatePriceListCommand 매핑")
    void toCommand() {
        // given
        UpdatePriceReferenceImageRequest imageWebRequest = UpdatePriceReferenceImageRequest.builder()
                .priceRefImageNo("img-001")
                .fileKey("file-key-001")
                .imageOrder(1)
                .status(ChangeStatus.NEW)
                .build();

        UpdatePackageRequest updatePackageRequest = UpdatePackageRequest.builder()
                .packageNo("pkg-001")
                .name("패키지A")
                .price(10000)
                .contents(List.of("내용1", "내용2"))
                .notice("주의사항")
                .status(ChangeStatus.NEW)
                .build();

        UpdateOptionRequest updateOptionRequest = UpdateOptionRequest.builder()
                .optionNo("opt-001")
                .name("옵션A")
                .count(2)
                .price(3000)
                .contents(List.of("옵션내용"))
                .status(ChangeStatus.NEW)
                .build();

        UpdatePriceWebReq updatePriceWebRequest = UpdatePriceWebReq.builder()
                .priceNo("price-001")
                .priceThemeType("THEME")
                .priceReferenceImages(List.of(imageWebRequest))
                .packages(List.of(updatePackageRequest))
                .options(List.of(updateOptionRequest))
                .status(ChangeStatus.NEW)
                .build();

        UpdatePriceListRequest webRequest = new UpdatePriceListRequest(List.of(updatePriceWebRequest));

        // when
        UpdatePriceListCommand command = mapper.toCommand(webRequest);

        // then
        assertThat(command).isNotNull();

        // PriceCommand의 모든 필드 검증
        assertThat(command.prices()).hasSize(1);
        UpdatePriceAppReq updatePriceAppReq = command.prices().getFirst();
        assertThat(updatePriceAppReq.priceNo()).isEqualTo("price-001");
        assertThat(updatePriceAppReq.priceThemeType()).isEqualTo("THEME");
        assertThat(updatePriceAppReq.status()).isEqualTo(ChangeStatus.NEW);

        // UpdatePriceReferenceImageCommand 모든 필드 검증
        assertThat(updatePriceAppReq.priceReferenceImages()).hasSize(1);
        UpdatePriceReferenceImageCommand imageCommand = updatePriceAppReq.priceReferenceImages().getFirst();
        assertThat(imageCommand.priceRefImageNo()).isEqualTo("img-001");
        assertThat(imageCommand.fileKey()).isEqualTo("file-key-001");
        assertThat(imageCommand.imageOrder()).isEqualTo(1);
        assertThat(imageCommand.status()).isEqualTo(ChangeStatus.NEW);

        // UpdatePackageCommand 모든 필드 검증
        assertThat(updatePriceAppReq.packages()).hasSize(1);
        UpdatePackageCommand updatePackageCommand = updatePriceAppReq.packages().getFirst();
        assertThat(updatePackageCommand.packageNo()).isEqualTo("pkg-001");
        assertThat(updatePackageCommand.name()).isEqualTo("패키지A");
        assertThat(updatePackageCommand.price()).isEqualTo(10000);
        assertThat(updatePackageCommand.contents()).containsExactly("내용1", "내용2");
        assertThat(updatePackageCommand.notice()).isEqualTo("주의사항");
        assertThat(updatePackageCommand.status()).isEqualTo(ChangeStatus.NEW);

        // UpdateOptionCommand 모든 필드 검증
        assertThat(updatePriceAppReq.options()).hasSize(1);
        UpdateOptionCommand updateOptionCommand = updatePriceAppReq.options().getFirst();
        assertThat(updateOptionCommand.optionNo()).isEqualTo("opt-001");
        assertThat(updateOptionCommand.name()).isEqualTo("옵션A");
        assertThat(updateOptionCommand.count()).isEqualTo(2);
        assertThat(updateOptionCommand.price()).isEqualTo(3000);
        assertThat(updateOptionCommand.contents()).containsExactly("옵션내용");
        assertThat(updateOptionCommand.status()).isEqualTo(ChangeStatus.NEW);
    }
}