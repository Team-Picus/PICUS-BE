package com.picus.core.user.adapter.in;

import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.in.web.data.request.SaveTermRequest;
import com.picus.core.user.adapter.in.web.data.response.GetTermResponse;
import com.picus.core.user.adapter.in.web.mapper.TermWebMapper;
import com.picus.core.user.application.port.in.TermManagementUseCase;
import com.picus.core.user.application.port.in.command.SaveTermCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/terms")
public class TermController {

    private final TermManagementUseCase termManagementUseCase;
    private final TermWebMapper termWebMapper;

    @PostMapping
    public BaseResponse<Void> approve(@CurrentUser String userNo, @RequestBody List<SaveTermRequest> requests) {
        List<SaveTermCommand> commands = requests.stream()
                .map(termWebMapper::toCommand)
                .toList();

        termManagementUseCase.approve(userNo, commands);
        return BaseResponse.onSuccess();
    }

    @GetMapping
    public BaseResponse<List<GetTermResponse>> getTerms() {
        List<GetTermResponse> response = termManagementUseCase.getTerms().stream()
                .map(termWebMapper::toResponse)
                .toList();

        return BaseResponse.onSuccess(response);
    }
}
