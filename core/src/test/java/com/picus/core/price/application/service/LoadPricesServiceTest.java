package com.picus.core.price.application.service;

import com.picus.core.price.application.port.out.PriceQueryPort;
import com.picus.core.price.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;


class LoadPricesServiceTest {

    private final PriceQueryPort priceQueryPort = Mockito.mock(PriceQueryPort.class);
    private final LoadPriceService pricesByExpertQueryService = new LoadPriceService(priceQueryPort);

    @Test
    @DisplayName("특정 전문가의 가격정보를 조회하는 서비스 메서드의 리턴값 및 상호작용을 검증한다.")
    public void loadByExpertNo() throws Exception {
        // given
        String testExpertNo = "test_expert_no";
        Price mockPrice = Mockito.mock(Price.class);
        List<Price> mockPriceList = List.of(mockPrice);
        BDDMockito.given(priceQueryPort.findByExpertNo(testExpertNo))
                .willReturn(mockPriceList);

        // when
        List<Price> result = priceQueryPort.findByExpertNo(testExpertNo);

        // then
        assertThat(result).isEqualTo(mockPriceList);
        then(priceQueryPort).should().findByExpertNo(testExpertNo);
    }

}