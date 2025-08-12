package com.picus.core.price.application.service;

import com.picus.core.price.application.port.out.PriceReadPort;
import com.picus.core.price.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class LoadMyPackageServiceTest {

    @Mock
    private PriceReadPort priceReadPort;

    @InjectMocks
    private LoadMyPackageService service;

    @Test
    @DisplayName("나의 패키지 정보를 조회한다.")
    public void load() throws Exception {
        // given
        String currentUserNo = "user-123";

        Price mockPrice = mock(Price.class);
        given(priceReadPort.findByExpertNo(currentUserNo)).willReturn(List.of(mockPrice));

        // when
        List<Price> results = service.load(currentUserNo);

        // then
        assertThat(results).hasSize(1)
                .containsExactly(mockPrice);

        then(priceReadPort).should().findByExpertNo(currentUserNo);
    }

}