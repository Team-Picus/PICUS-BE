package com.picus.core.price.application.service;

import com.picus.core.price.application.port.out.PriceReadPort;
import com.picus.core.price.domain.Price;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class LoadMyPackageServiceTest {

    @Mock
    private UserReadPort userReadPort;
    @Mock
    private PriceReadPort priceReadPort;

    @InjectMocks
    private LoadMyPackageService service;

    @Test
    @DisplayName("나의 패키지 정보를 조회한다.")
    public void load() throws Exception {
        // given
        String currentUserNo = "user-123";
        String expertNo = "expert-123";

        User mockUser = createMockUser(currentUserNo, expertNo);
        given(userReadPort.findById(currentUserNo)).willReturn(mockUser);

        Price mockPrice = mock(Price.class);
        given(priceReadPort.findByExpertNo(expertNo)).willReturn(List.of(mockPrice));

        // when
        List<Price> results = service.load(currentUserNo);

        // then
        assertThat(results).hasSize(1)
                .containsExactly(mockPrice);

        then(userReadPort).should().findById(currentUserNo);
        then(priceReadPort).should().findByExpertNo(expertNo);
    }

    private User createMockUser(String currentUserNo, String expertNo) {
        return User.builder()
                .userNo(currentUserNo)
                .expertNo(expertNo)
                .build();
    }

}