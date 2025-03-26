package com.picus.core.domain.expert.application.usecase;

import com.picus.core.domain.expert.application.dto.request.RegExpReq;
import com.picus.core.domain.expert.domain.entity.Expert;
import com.picus.core.domain.expert.domain.entity.area.ExpertDistrict;
import com.picus.core.domain.expert.domain.service.ExpertDistrictService;
import com.picus.core.domain.expert.domain.service.ExpertService;
import com.picus.core.domain.shared.area.domain.entity.District;
import com.picus.core.domain.shared.area.domain.service.AreaSearchService;
import com.picus.core.global.utils.regex.BadWordFilterUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ExpertInfoUseCase {

    private final AreaSearchService areaSearchService;
    private final ExpertDistrictService expertDistrictService;
    private final ExpertService expertService;
    private final BadWordFilterUtil badWordFilterUtil;

    @Transactional
    public Expert save(Long userNo, RegExpReq request) {
        filterBadWord(request);

        Expert expert = expertService.save(userNo, request);
        Set<District> districts = areaSearchService.findDistricts(request.activityAreas());
        Set<ExpertDistrict> expertDistricts = expertDistrictService.save(expert, districts);
        expert.updateActivityAreas(expertDistricts);

        return expert;
    }

    private void filterBadWord(RegExpReq request) {
        badWordFilterUtil.filterBadWord(request.intro());
        badWordFilterUtil.filterBadWord(request.career());
        request.skills().iterator().forEachRemaining(badWordFilterUtil::filterBadWord);
    }
}
