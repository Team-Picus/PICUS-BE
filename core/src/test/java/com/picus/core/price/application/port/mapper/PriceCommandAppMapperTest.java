package com.picus.core.price.application.port.mapper;

import com.picus.core.expert.domain.model.vo.PriceThemeType;
import com.picus.core.price.application.port.in.command.*;
import com.picus.core.price.domain.model.*;
import com.picus.core.price.domain.model.Package;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PriceCommandAppMapperTest {

    private final PriceCommandAppMapper mapper = new PriceCommandAppMapper();

    @Test
    @DisplayName("Price 도메인 모델로 전환한다.")
    void toPriceDomain_success() {
        // given
        List<PriceReferenceImageCommand> imageCommands = List.of(
                new PriceReferenceImageCommand("img-001", "https://cdn.com/1.jpg", 1, ChangeStatus.NEW),
                new PriceReferenceImageCommand("img-002", "https://cdn.com/2.jpg", 2, ChangeStatus.UPDATE)
        );

        List<PackageCommand> packageCommands = List.of(
                new PackageCommand("pkg-001", "패키지1", 100000, List.of("내용1", "내용2"), "주의1", ChangeStatus.NEW),
                new PackageCommand("pkg-002", "패키지2", 150000, List.of("내용3"), "주의2", ChangeStatus.UPDATE)
        );

        List<OptionCommand> optionCommands = List.of(
                new OptionCommand("opt-001", "옵션1", 1, 30000, List.of("옵션설명1"), ChangeStatus.NEW),
                new OptionCommand("opt-002", "옵션2", 2, 50000, List.of("옵션설명2"), ChangeStatus.UPDATE)
        );

        PriceCommand priceCommand = new PriceCommand(
                "price-123",
                "SNAP",
                imageCommands,
                packageCommands,
                optionCommands,
                ChangeStatus.UPDATE
        );

        // when
        Price result = mapper.toPriceDomain(priceCommand);

        // then
        assertThat(result.getPriceNo()).isEqualTo("price-123");
        assertThat(result.getPriceThemeType()).isEqualTo(PriceThemeType.SNAP);

        assertThat(result.getPriceReferenceImages()).hasSize(2);
        assertThat(result.getPriceReferenceImages().getFirst().getFileKey()).isEqualTo("https://cdn.com/1.jpg");

        assertThat(result.getPackages()).hasSize(2);
        assertThat(result.getPackages().get(1).getName()).isEqualTo("패키지2");

        assertThat(result.getOptions()).hasSize(2);
        assertThat(result.getOptions().getFirst().getPrice()).isEqualTo(30000);
    }
}