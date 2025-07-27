package com.picus.core.post.adapter.in;

import com.picus.core.post.application.port.in.DeletePostUseCase;
import com.picus.core.post.application.port.out.DeletePostPort;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class DeletePostController {

    private final DeletePostUseCase deletePostUseCase;

    @DeleteMapping("/{post_no}")
    public BaseResponse<Void> delete(@PathVariable("post_no") String postNo, @CurrentUser String userNo) {
        deletePostUseCase.delete(postNo, userNo);
        return BaseResponse.onSuccess();
    }

}
