package com.picus.core.price.adapter.in;

import com.picus.core.price.adapter.in.web.data.request.UpdatePriceListWebReq;
import com.picus.core.price.adapter.in.web.mapper.UpdatePriceWebMapper;
import com.picus.core.price.application.port.in.UpdatePriceCommand;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdatePriceController {

    private final UpdatePriceCommand updatePriceCommand;
    private final UpdatePriceWebMapper webMapper;

    @PatchMapping("/api/v1/experts/prices")
    public BaseResponse<Void> applyPriceChanges(@RequestBody @Valid UpdatePriceListWebReq webRequest,
                                                @CurrentUser String userNo) {
        updatePriceCommand.update(webMapper.toCommand(webRequest), userNo);
        return BaseResponse.onSuccess();
    }
}
