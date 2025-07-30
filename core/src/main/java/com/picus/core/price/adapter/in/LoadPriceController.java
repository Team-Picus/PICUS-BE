package com.picus.core.price.adapter.in;

import com.picus.core.price.adapter.in.web.data.response.LoadPriceResponse;
import com.picus.core.price.adapter.in.web.mapper.LoadPriceWebMapper;
import com.picus.core.price.application.port.in.LoadPriceUseCase;
import com.picus.core.price.domain.Price;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/experts")
public class LoadPriceController {

    private final LoadPriceUseCase loadPriceUseCase;
    private final LoadPriceWebMapper loadPriceWebMapper;

    @GetMapping("/{expert_no}/prices")
    public BaseResponse<List<LoadPriceResponse>> getPricesByExpert(@PathVariable("expert_no") String expertNo) {

        List<Price> pricesByExpert = loadPriceUseCase.loadByExpertNo(expertNo); // 유스케이스 호출

        List<LoadPriceResponse> webResponses = pricesByExpert.stream() // 매퍼로 변환
                .map(loadPriceWebMapper::toWebResponse)
                .toList();
        return BaseResponse.onSuccess(webResponses);
    }
}
