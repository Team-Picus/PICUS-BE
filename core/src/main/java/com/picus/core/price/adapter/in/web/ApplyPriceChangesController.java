package com.picus.core.price.adapter.in.web;

import com.picus.core.price.adapter.in.web.data.request.ApplyPriceChangesWebRequest;
import com.picus.core.price.adapter.in.web.mapper.ApplyPriceChangesWebMapper;
import com.picus.core.price.application.port.in.PriceInfoCommand;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApplyPriceChangesController {

    private final PriceInfoCommand priceInfoCommand;
    private final ApplyPriceChangesWebMapper webMapper;

    @PatchMapping("/api/v1/experts/prices")
    public BaseResponse<Void> applyPriceChanges(@RequestBody @Valid ApplyPriceChangesWebRequest webRequest,
                                                @CurrentUser String userNo) {
        priceInfoCommand.apply(webMapper.toCommand(webRequest), userNo);
        return BaseResponse.onSuccess();
    }
}
