package com.picus.core.post.adapter.in;

import com.picus.core.post.adapter.in.web.mapper.LoadGalleryWebMapper;
import com.picus.core.post.application.port.in.LoadGalleryUseCase;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/experts")
@RequiredArgsConstructor
public class LoadGalleryController {

    private final LoadGalleryUseCase loadGalleryUseCase;
    private final LoadGalleryWebMapper webMapper;

    @GetMapping("/posts/{expert_no}/gallery")
    public BaseResponse<Object> loadGallery(@PathVariable("expert_no") String expertNo) {
        return loadGalleryUseCase.load(expertNo)
                .map(resp -> (Object) webMapper.toWebResp(resp))
                .map(BaseResponse::onSuccess)
                .orElseGet(() -> BaseResponse.onSuccess("아직 갤러리가 설정되지 않았습니다."));
    }
}
