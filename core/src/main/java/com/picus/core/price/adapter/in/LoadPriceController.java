package com.picus.core.price.adapter.in;

import com.picus.core.price.adapter.in.web.data.response.LoadPriceResponse;
import com.picus.core.price.adapter.in.web.mapper.LoadPriceWebMapper;
import com.picus.core.price.application.port.in.LoadPriceUseCase;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.domain.vo.SnapSubTheme;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/experts")
public class LoadPriceController {

    private final LoadPriceUseCase loadPriceUseCase;
    private final LoadPriceWebMapper webMapper;

    @GetMapping("/{expert_no}/prices")
    public BaseResponse<LoadPriceResponse> getPricesByExpert(
            @PathVariable("expert_no") String expertNo,
            @RequestParam(required = false) List<PriceThemeType> priceThemeTypes,
            @RequestParam(required = false) List<SnapSubTheme> snapSubThemes
    ) {

        List<Price> prices = loadPriceUseCase.loadByExpertNo(expertNo, priceThemeTypes, snapSubThemes); // 유스케이스 호출

        LoadPriceResponse response = webMapper.toResponse(prices); // 매핑

        return BaseResponse.onSuccess(response);
    }
}
