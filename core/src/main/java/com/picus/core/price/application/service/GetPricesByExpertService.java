package com.picus.core.price.application.service;

import com.picus.core.price.application.port.in.GetPricesByExpertQuery;
import com.picus.core.price.application.port.out.LoadPricePort;
import com.picus.core.price.domain.model.Price;
import com.picus.core.shared.annotation.UseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@UseCase
public class GetPricesByExpertService implements GetPricesByExpertQuery {

    private final LoadPricePort loadPricePort;

    @Override
    public List<Price> getPricesByExpert(String expertNo) {
        // 특정 ExpertNo를 가진 Price를 가져옴
        // TODO:PriceReferenceImage의 fileKey -> url 변환 필요
        return loadPricePort.findByExpertNo(expertNo);
    }
}
