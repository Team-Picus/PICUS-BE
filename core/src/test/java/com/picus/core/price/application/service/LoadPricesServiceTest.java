package com.picus.core.price.application.service;

import com.picus.core.price.application.port.out.PriceReadPort;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.domain.vo.SnapSubTheme;
import com.picus.core.shared.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class LoadPricesServiceTest {

    @Mock
    private PriceReadPort priceReadPort = Mockito.mock(PriceReadPort.class);
    @InjectMocks
    private LoadPriceService service;

    @Test
    @DisplayName("특정 전문가의 가격정보를 조회하는 서비스 메서드의 리턴값 및 상호작용을 검증한다.")
    public void loadByExpertNo() throws Exception {
        // given
        String testExpertNo = "test_expert_no";
        List<PriceThemeType> priceThemeTypes = List.of(PriceThemeType.SNAP);
        List<SnapSubTheme> snapSubThemes = List.of(SnapSubTheme.FAMILY);

        Price mockPrice = Mockito.mock(Price.class);
        List<Price> mockPriceList = List.of(mockPrice);
        BDDMockito.given(priceReadPort.findByExpertNoAndThemes(testExpertNo, priceThemeTypes, snapSubThemes))
                .willReturn(mockPriceList);

        // when
        List<Price> result = service.loadByExpertNo(testExpertNo, priceThemeTypes, snapSubThemes);

        // then
        assertThat(result).isEqualTo(mockPriceList);
        then(priceReadPort).should().findByExpertNoAndThemes(testExpertNo, priceThemeTypes, snapSubThemes);
    }

    @Test
    @DisplayName("SNAP 테마가 설정되어 있으나 세부 테마(snapSubThemes)가 비어있는 경우 에러가 발생한다.")
    public void loadByExpertNo_fail1() throws Exception {
        // given
        String testExpertNo = "test_expert_no";
        List<PriceThemeType> priceThemeTypes = List.of(PriceThemeType.SNAP);

        // when // then
        assertThatThrownBy(() -> service.loadByExpertNo(testExpertNo, priceThemeTypes, null))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("SNAP 테마가 없는데 세부 테마(snapSubThemes)가 존재하는 경우 에러가 발생한다.")
    public void loadByExpertNo_fail2() throws Exception {
        // given
        String testExpertNo = "test_expert_no";
        List<PriceThemeType> priceThemeTypes = List.of(PriceThemeType.BEAUTY);
        List<SnapSubTheme> snapSubThemes = List.of(SnapSubTheme.FAMILY);

        // when // then
        assertThatThrownBy(() -> service.loadByExpertNo(testExpertNo, priceThemeTypes, snapSubThemes))
                .isInstanceOf(RestApiException.class);
    }

}