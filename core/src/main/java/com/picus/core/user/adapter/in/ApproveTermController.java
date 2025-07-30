package com.picus.core.user.adapter.in;

import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.in.web.data.request.ApproveTermRequest;
import com.picus.core.user.adapter.in.web.mapper.TermWebMapper;
import com.picus.core.user.application.port.in.ApproveTermUseCase;
import com.picus.core.user.application.port.in.command.SaveUserTermCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/terms")
public class ApproveTermController {

    private final ApproveTermUseCase approveTermUseCase;
    private final TermWebMapper termWebMapper;

    @PostMapping
    public BaseResponse<Void> approve(
            @CurrentUser String userNo,
            @RequestBody List<ApproveTermRequest> requests
    ) {
        List<SaveUserTermCommand> commands = requests.stream()
                .map(termWebMapper::toCommand)
                .toList();

        approveTermUseCase.approve(userNo, commands);
        return BaseResponse.onSuccess();
    }
}
