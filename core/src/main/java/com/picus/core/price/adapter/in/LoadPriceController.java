package com.picus.core.price.adapter.in;

import com.picus.core.price.adapter.in.web.data.response.LoadPriceWebResponse;
import com.picus.core.price.adapter.in.web.mapper.LoadPriceWebMapper;
import com.picus.core.price.application.port.in.LoadPriceQuery;
import com.picus.core.price.domain.Price;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadPriceController {

    private final LoadPriceQuery loadPriceQuery;
    private final LoadPriceWebMapper loadPriceWebMapper;

    @GetMapping("/api/v1/experts/{expert_no}/prices")
    public BaseResponse<List<LoadPriceWebResponse>> getPricesByExpert(@PathVariable("expert_no") String expertNo) {

        List<Price> pricesByExpert = loadPriceQuery.loadByExpertNo(expertNo); // 유스케이스 호출

        List<LoadPriceWebResponse> webResponses = pricesByExpert.stream() // 매퍼로 변환
                .map(loadPriceWebMapper::toWebResponse)
                .toList();
        return BaseResponse.onSuccess(webResponses);
    }
}
