package com.picus.core.domain.studio.application.usecase;

import com.picus.core.domain.expert.domain.service.ExpertService;
import com.picus.core.domain.studio.application.dto.request.StudioReq;
import com.picus.core.domain.studio.domain.entity.Studio;
import com.picus.core.domain.studio.domain.service.StudioService;
import com.picus.core.global.common.exception.RestApiException;
import com.picus.core.global.utils.regex.BadWordFilterUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class StudioInfoUseCase {

    private final ExpertService expertService;
    private final StudioService studioService;
    private final BadWordFilterUtil badWordFilterUtil;

    public Studio save(Long expertNo, StudioReq request) {
        if (expertService.isExist(expertNo))
            throw new RestApiException(_NOT_FOUND);

        badWordFilterUtil.filterBadWord(request.name());

        return studioService.save(expertNo, request);
    }
}
