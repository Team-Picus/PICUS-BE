package com.picus.core.price.adapter.in.web;

import com.picus.core.price.adapter.in.web.data.response.GetPricesByExpertWebResponse;
import com.picus.core.price.adapter.in.web.mapper.GetPricesByExpertWebMapper;
import com.picus.core.price.application.port.in.GetPricesByExpertQuery;
import com.picus.core.price.domain.model.Price;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetPricesByExpertController {

    private final GetPricesByExpertQuery getPricesByExpertQuery;
    private final GetPricesByExpertWebMapper getPricesByExpertWebMapper;

    @GetMapping("/api/v1/experts/{expert_no}/prices")
    public BaseResponse<List<GetPricesByExpertWebResponse>> getPricesByExpert(@PathVariable("expert_no") String expertNo) {

        List<Price> pricesByExpert = getPricesByExpertQuery.getPricesByExpert(expertNo); // 유스케이스 호출

        List<GetPricesByExpertWebResponse> webResponses = pricesByExpert.stream() // 매퍼로 변환
                .map(getPricesByExpertWebMapper::toWebResponse)
                .toList();
        return BaseResponse.onSuccess(webResponses);
    }
}
