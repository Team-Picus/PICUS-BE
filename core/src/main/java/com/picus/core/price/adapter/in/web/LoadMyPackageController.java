package com.picus.core.price.adapter.in.web;

import com.picus.core.price.adapter.in.web.data.response.LoadMyPackageResponse;
import com.picus.core.price.adapter.in.web.mapper.LoadMyPackageWebMapper;
import com.picus.core.price.application.port.in.LoadMyPackageUseCase;
import com.picus.core.price.domain.Price;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/experts")
public class LoadMyPackageController {

    private final LoadMyPackageUseCase loadMyPackageUseCase;
    private final LoadMyPackageWebMapper webMapper;

    @GetMapping("/prices/packages/me")
    public BaseResponse<LoadMyPackageResponse> load(@CurrentUser String userNo) {
        List<Price> prices = loadMyPackageUseCase.load(userNo);
        LoadMyPackageResponse responses = webMapper.toResponse(prices);
        return BaseResponse.onSuccess(responses);
    }
}
