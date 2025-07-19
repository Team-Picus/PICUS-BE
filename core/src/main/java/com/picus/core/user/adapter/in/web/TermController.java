package com.picus.core.user.adapter.in.web;

import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.in.web.data.response.GetTermResponse;
import com.picus.core.user.application.port.in.TermManagementUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/terms")
public class TermController {

    private final TermManagementUseCase termManagementUseCase;

    @PostMapping
    public BaseResponse<Void> approve() {

        return BaseResponse.onSuccess();
    }

    @GetMapping
    public BaseResponse<List<GetTermResponse>> getTerms() {
        return BaseResponse.onSuccess(termManagementUseCase.getTerms());
    }
}
