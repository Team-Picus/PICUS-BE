package com.picus.core.price.application.service;

import com.picus.core.price.application.port.in.LoadPriceUseCase;
import com.picus.core.price.application.port.out.ReadPricePort;
import com.picus.core.price.domain.Price;
import com.picus.core.shared.annotation.UseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@UseCase
public class LoadPriceService implements LoadPriceUseCase {

    private final ReadPricePort readPricePort;

    @Override
    public List<Price> loadByExpertNo(String expertNo) {
        // 특정 ExpertNo를 가진 Price를 가져옴
        // TODO:PriceReferenceImage의 fileKey -> url 변환 필요
        return readPricePort.findByExpertNo(expertNo);
    }
}
