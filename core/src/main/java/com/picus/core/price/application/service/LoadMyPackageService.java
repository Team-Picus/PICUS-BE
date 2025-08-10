package com.picus.core.price.application.service;

import com.picus.core.price.application.port.in.LoadMyPackageUseCase;
import com.picus.core.price.application.port.out.PriceReadPort;
import com.picus.core.price.domain.Price;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._FORBIDDEN;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadMyPackageService implements LoadMyPackageUseCase {

    private final UserReadPort userReadPort;

    private final PriceReadPort priceReadPort;

    @Override
    public List<Price> load(String currentUserNo) {
        // userNo로 expertNo 조회
        User user = userReadPort.findById(currentUserNo);
        String expertNo =  Optional.ofNullable(user.getExpertNo())
                .orElseThrow(() -> new RestApiException(_FORBIDDEN));

        // 해당 expertNo의 가격정보 조회
        return priceReadPort.findByExpertNo(expertNo);
    }

}
