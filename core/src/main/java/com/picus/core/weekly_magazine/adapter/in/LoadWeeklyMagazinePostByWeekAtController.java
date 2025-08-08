package com.picus.core.weekly_magazine.adapter.in;

import com.picus.core.shared.common.BaseResponse;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.weekly_magazine.adapter.in.web.data.response.LoadWeeklyMagazinePostByWeekAtResponse;
import com.picus.core.weekly_magazine.adapter.in.web.mapper.LoadWeeklyMagazinePostByWeekAtWebMapper;
import com.picus.core.weekly_magazine.application.port.in.LoadWeeklyMagazinePostByWeekAtUseCase;
import com.picus.core.weekly_magazine.application.port.in.result.LoadWeeklyMagazinePostByWeekAtResult;
import com.picus.core.weekly_magazine.domain.model.vo.WeekAt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._BAD_REQUEST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weekly_magazine")
public class LoadWeeklyMagazinePostByWeekAtController {

    private final LoadWeeklyMagazinePostByWeekAtUseCase useCase;

    private final LoadWeeklyMagazinePostByWeekAtWebMapper webMapper;

    @GetMapping("/{year}/{month}/{week}/posts")
    public BaseResponse<LoadWeeklyMagazinePostByWeekAtResponse> load(
            @PathVariable int year, @PathVariable int month, @PathVariable int week) {
        WeekAt weekAt = null;
        try {
            weekAt = WeekAt.builder()
                    .year(year)
                    .month(month)
                    .week(week)
                    .build();
        } catch (IllegalStateException e) {
            throw new RestApiException(_BAD_REQUEST);
        }

        List<LoadWeeklyMagazinePostByWeekAtResult> results = useCase.load(weekAt);

        return BaseResponse.onSuccess(webMapper.toResponse(results));
    }
}
