package com.picus.core.post.adapter.in;

import com.picus.core.post.adapter.in.web.data.request.SearchPostRequest;
import com.picus.core.post.adapter.in.web.data.response.SearchPostResponse;
import com.picus.core.post.application.port.in.SearchPostUseCase;
import com.picus.core.post.adapter.in.web.mapper.SearchPostWebMapper;
import com.picus.core.post.application.port.in.result.SearchPostResult;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.shared.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._BAD_REQUEST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class SearchPostController {

    private final SearchPostUseCase searchPostUseCase;
    private final SearchPostWebMapper webMapper;

    private static final List<String> ALLOWED_SORT_BY = List.of("updatedAt");
    private static final List<String> ALLOWED_SORT_DIRECTION = List.of("ASC", "DESC");

    @GetMapping("/search/results")
    public BaseResponse<SearchPostResponse> search(SearchPostRequest request) {
        validateSort(request.getSortBy(), request.getSortDirection());

        SearchPostResult result = searchPostUseCase.search(webMapper.toCommand(request));

        return BaseResponse.onSuccess(webMapper.toResponse(result));
    }


    private void validateSort(String sortBy, String sortDirection) {
        if (!ALLOWED_SORT_BY.contains(sortBy)) {
            throw new RestApiException(_BAD_REQUEST);
        }

        if (!ALLOWED_SORT_DIRECTION.contains(sortDirection.toUpperCase())) {
            throw new RestApiException(_BAD_REQUEST);
        }
    }
}
