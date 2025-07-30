package com.picus.core.user.adapter.in;

import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.in.web.data.request.SignUpRequest;
import com.picus.core.user.adapter.in.web.mapper.SignUpWebMapper;
import com.picus.core.user.application.port.in.CompleteSignUpUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class CompleteSignUpController {

    private final CompleteSignUpUseCase completeSignUpUseCase;
    private final SignUpWebMapper webMapper;

    @PatchMapping("/sign-up/complete")
    public BaseResponse<Void> complete(
            @CurrentUser String userNo,
            @RequestBody SignUpRequest request
    ) {
        completeSignUpUseCase.complete(webMapper.toCommand(userNo, request));
        return BaseResponse.onSuccess();
    }
}
