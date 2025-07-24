package com.picus.core.price.adapter.in.web.mapper;

import com.picus.core.price.adapter.in.web.data.request.*;
import com.picus.core.price.application.port.in.request.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdatePriceWebMapperTest {

    private final UpdatePriceWebMapper mapper = new UpdatePriceWebMapper();

    @Test
    @DisplayName("UpdatePriceListWebReq -> UpdatePriceListAppReq 매핑")
    void toCommand() {
        // given
        UpdatePriceReferenceImageWebReq imageWebRequest = UpdatePriceReferenceImageWebReq.builder()
                .priceRefImageNo("img-001")
                .fileKey("file-key-001")
                .imageOrder(1)
                .status(ChangeStatus.NEW)
                .build();

        UpdatePackageWebReq updatePackageWebReq = UpdatePackageWebReq.builder()
                .packageNo("pkg-001")
                .name("패키지A")
                .price(10000)
                .contents(List.of("내용1", "내용2"))
                .notice("주의사항")
                .status(ChangeStatus.NEW)
                .build();

        UpdateOptionWebReq updateOptionWebReq = UpdateOptionWebReq.builder()
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
                .packages(List.of(updatePackageWebReq))
                .options(List.of(updateOptionWebReq))
                .status(ChangeStatus.NEW)
                .build();

        UpdatePriceListWebReq webRequest = new UpdatePriceListWebReq(List.of(updatePriceWebRequest));

        // when
        UpdatePriceListAppReq command = mapper.toCommand(webRequest);

        // then
        assertThat(command).isNotNull();

        // PriceCommand의 모든 필드 검증
        assertThat(command.prices()).hasSize(1);
        UpdatePriceAppReq updatePriceAppReq = command.prices().getFirst();
        assertThat(updatePriceAppReq.priceNo()).isEqualTo("price-001");
        assertThat(updatePriceAppReq.priceThemeType()).isEqualTo("THEME");
        assertThat(updatePriceAppReq.status()).isEqualTo(ChangeStatus.NEW);

        // UpdatePriceReferenceImageAppReq 모든 필드 검증
        assertThat(updatePriceAppReq.priceReferenceImages()).hasSize(1);
        UpdatePriceReferenceImageAppReq imageCommand = updatePriceAppReq.priceReferenceImages().getFirst();
        assertThat(imageCommand.priceRefImageNo()).isEqualTo("img-001");
        assertThat(imageCommand.fileKey()).isEqualTo("file-key-001");
        assertThat(imageCommand.imageOrder()).isEqualTo(1);
        assertThat(imageCommand.status()).isEqualTo(ChangeStatus.NEW);

        // UpdatePackageAppReq 모든 필드 검증
        assertThat(updatePriceAppReq.packages()).hasSize(1);
        UpdatePackageAppReq updatePackageAppReq = updatePriceAppReq.packages().getFirst();
        assertThat(updatePackageAppReq.packageNo()).isEqualTo("pkg-001");
        assertThat(updatePackageAppReq.name()).isEqualTo("패키지A");
        assertThat(updatePackageAppReq.price()).isEqualTo(10000);
        assertThat(updatePackageAppReq.contents()).containsExactly("내용1", "내용2");
        assertThat(updatePackageAppReq.notice()).isEqualTo("주의사항");
        assertThat(updatePackageAppReq.status()).isEqualTo(ChangeStatus.NEW);

        // UpdateOptionAppReq 모든 필드 검증
        assertThat(updatePriceAppReq.options()).hasSize(1);
        UpdateOptionAppReq updateOptionAppReq = updatePriceAppReq.options().getFirst();
        assertThat(updateOptionAppReq.optionNo()).isEqualTo("opt-001");
        assertThat(updateOptionAppReq.name()).isEqualTo("옵션A");
        assertThat(updateOptionAppReq.count()).isEqualTo(2);
        assertThat(updateOptionAppReq.price()).isEqualTo(3000);
        assertThat(updateOptionAppReq.contents()).containsExactly("옵션내용");
        assertThat(updateOptionAppReq.status()).isEqualTo(ChangeStatus.NEW);
    }
}