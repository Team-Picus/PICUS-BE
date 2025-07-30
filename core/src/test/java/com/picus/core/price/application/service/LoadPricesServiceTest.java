package com.picus.core.price.application.service;

import com.picus.core.price.application.port.out.PriceReadPort;
import com.picus.core.price.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;


class LoadPricesServiceTest {

    private final PriceReadPort priceReadPort = Mockito.mock(PriceReadPort.class);
    private final LoadPriceService pricesByExpertQueryService = new LoadPriceService(priceReadPort);

    @Test
    @DisplayName("특정 전문가의 가격정보를 조회하는 서비스 메서드의 리턴값 및 상호작용을 검증한다.")
    public void loadByExpertNo() throws Exception {
        // given
        String testExpertNo = "test_expert_no";
        Price mockPrice = Mockito.mock(Price.class);
        List<Price> mockPriceList = List.of(mockPrice);
        BDDMockito.given(priceReadPort.findByExpertNo(testExpertNo))
                .willReturn(mockPriceList);

        // when
        List<Price> result = priceReadPort.findByExpertNo(testExpertNo);

        // then
        assertThat(result).isEqualTo(mockPriceList);
        then(priceReadPort).should().findByExpertNo(testExpertNo);
    }

}