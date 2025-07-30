package com.picus.core.user.adapter.in;

import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.in.web.data.response.LoadTermResponse;
import com.picus.core.user.adapter.in.web.mapper.TermWebMapper;
import com.picus.core.user.application.port.in.LoadTermUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/terms")
public class LoadTermController {

    private final LoadTermUseCase loadTermUseCase;
    private final TermWebMapper termWebMapper;

    @GetMapping
    public BaseResponse<List<LoadTermResponse>> load() {
        List<LoadTermResponse> response = loadTermUseCase.load().stream()
                .map(termWebMapper::toResponse)
                .toList();

        return BaseResponse.onSuccess(response);
    }
}
