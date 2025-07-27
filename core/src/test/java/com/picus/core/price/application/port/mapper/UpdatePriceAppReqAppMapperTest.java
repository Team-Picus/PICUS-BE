package com.picus.core.price.application.port.mapper;

import com.picus.core.expert.domain.vo.PriceThemeType;
import com.picus.core.price.application.port.in.mapper.UpdatePriceAppMapper;
import com.picus.core.price.application.port.in.request.*;
import com.picus.core.price.domain.Option;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.PriceReferenceImage;
import com.picus.core.price.domain.Package;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UpdatePriceAppReqAppMapperTest {

    private final UpdatePriceAppMapper mapper = new UpdatePriceAppMapper();

    @Test
    @DisplayName("Price 도메인 모델로 전환한다 - 모든 필드 검증 (extracting 버전)")
    void toPriceDomain_success_모든필드검증() {
        // given
        List<UpdatePriceReferenceImageAppReq> imageCommands = List.of(
                new UpdatePriceReferenceImageAppReq("img-001", "https://cdn.com/1.jpg", 1, ChangeStatus.NEW),
                new UpdatePriceReferenceImageAppReq("img-002", "https://cdn.com/2.jpg", 2, ChangeStatus.UPDATE)
        );

        List<UpdatePackageAppReq> updatePackageAppReqs = List.of(
                new UpdatePackageAppReq("pkg-001", "패키지1", 100000, List.of("내용1", "내용2"), "주의1", ChangeStatus.NEW),
                new UpdatePackageAppReq("pkg-002", "패키지2", 150000, List.of("내용3"), "주의2", ChangeStatus.UPDATE)
        );

        List<UpdateOptionAppReq> updateOptionAppReqs = List.of(
                new UpdateOptionAppReq("opt-001", "옵션1", 1, 30000, List.of("옵션설명1"), ChangeStatus.NEW),
                new UpdateOptionAppReq("opt-002", "옵션2", 2, 50000, List.of("옵션설명2"), ChangeStatus.UPDATE)
        );

        UpdatePriceAppReq updatePriceAppReq = new UpdatePriceAppReq(
                "price-123",
                "SNAP",
                imageCommands,
                updatePackageAppReqs,
                updateOptionAppReqs,
                ChangeStatus.UPDATE
        );

        // when
        Price result = mapper.toPriceDomain(updatePriceAppReq);

        // then
        assertThat(result.getPriceNo()).isEqualTo("price-123");
        assertThat(result.getPriceThemeType()).isEqualTo(PriceThemeType.SNAP);

        // 이미지 필드 전체 검증
        assertThat(result.getPriceReferenceImages())
                .extracting(
                        PriceReferenceImage::getPriceRefImageNo,
                        PriceReferenceImage::getFileKey,
                        PriceReferenceImage::getImageOrder
                )
                .containsExactlyInAnyOrder(
                        tuple("img-001", "https://cdn.com/1.jpg", 1),
                        tuple("img-002", "https://cdn.com/2.jpg", 2)
                );

        // 패키지 필드 전체 검증
        assertThat(result.getPackages())
                .extracting(
                        Package::getPackageNo,
                        Package::getName,
                        Package::getPrice,
                        Package::getContents,
                        Package::getNotice
                )
                .containsExactlyInAnyOrder(
                        tuple("pkg-001", "패키지1", 100000, List.of("내용1", "내용2"), "주의1"),
                        tuple("pkg-002", "패키지2", 150000, List.of("내용3"), "주의2")
                );

        // 옵션 필드 전체 검증
        assertThat(result.getOptions())
                .extracting(
                        Option::getOptionNo,
                        Option::getName,
                        Option::getCount,
                        Option::getPrice,
                        Option::getContents
                )
                .containsExactlyInAnyOrder(
                        tuple("opt-001", "옵션1", 1, 30000, List.of("옵션설명1")),
                        tuple("opt-002", "옵션2", 2, 50000, List.of("옵션설명2"))
                );
    }
}