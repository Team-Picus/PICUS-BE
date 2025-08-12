package com.picus.core.price.application.service;

import com.picus.core.price.application.port.in.LoadMyPackageUseCase;
import com.picus.core.price.application.port.out.PriceReadPort;
import com.picus.core.price.domain.Price;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.out.UserReadPort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadMyPackageService implements LoadMyPackageUseCase {

    private final UserReadPort userReadPort;

    private final PriceReadPort priceReadPort;

    @Override
    public List<Price> load(String currentUserNo) {
        return priceReadPort.findByExpertNo(currentUserNo);
    }

}
