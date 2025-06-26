package com.picus.core.old.domain.studio.application.usecase;

import com.picus.core.old.domain.expert.domain.service.ExpertService;
import com.picus.core.old.domain.studio.application.dto.request.StudioReq;
import com.picus.core.old.domain.studio.domain.entity.Studio;
import com.picus.core.old.domain.studio.domain.service.StudioService;
import com.picus.core.old.global.common.exception.RestApiException;
import com.picus.core.old.global.utils.regex.BadWordFilterUtil;
import com.picus.core.old.global.common.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudioInfoUseCase {

    private final ExpertService expertService;
    private final StudioService studioService;
    private final BadWordFilterUtil badWordFilterUtil;

    @Transactional
    public Studio save(Long expertNo, StudioReq request) {
        if (!expertService.isExist(expertNo))
            throw new RestApiException(GlobalErrorStatus._NOT_FOUND);

        if(studioService.isExist(expertNo))
            throw new RestApiException(GlobalErrorStatus._EXIST_ENTITY);

        badWordFilterUtil.filterBadWord(request.name());

        return studioService.save(expertNo, request);
    }
}
