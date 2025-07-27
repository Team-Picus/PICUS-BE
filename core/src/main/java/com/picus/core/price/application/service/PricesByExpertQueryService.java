package com.picus.core.price.application.service;

import com.picus.core.price.application.port.in.PricesByExpertQuery;
import com.picus.core.price.application.port.out.PriceQueryPort;
import com.picus.core.price.domain.Price;
import com.picus.core.shared.annotation.UseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@UseCase
public class PricesByExpertQueryService implements PricesByExpertQuery {

    private final PriceQueryPort priceQueryPort;

    @Override
    public List<Price> getPricesByExpert(String expertNo) {
        // 특정 ExpertNo를 가진 Price를 가져옴
        // TODO:PriceReferenceImage의 fileKey -> url 변환 필요
        return priceQueryPort.findByExpertNo(expertNo);
    }
}
