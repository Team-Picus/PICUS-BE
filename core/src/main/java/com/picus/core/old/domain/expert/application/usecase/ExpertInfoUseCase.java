package com.picus.core.old.domain.expert.application.usecase;

import com.picus.core.old.domain.expert.application.dto.request.RegExpReq;
import com.picus.core.old.domain.expert.domain.entity.Expert;
import com.picus.core.old.domain.expert.domain.service.ExpertService;
import com.picus.core.old.domain.user.domain.entity.User;
import com.picus.core.old.domain.user.domain.entity.UserType;
import com.picus.core.old.domain.user.domain.service.UserService;
import com.picus.core.old.global.common.exception.RestApiException;
import com.picus.core.old.global.utils.regex.BadWordFilterUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.picus.core.old.global.common.exception.code.status.GlobalErrorStatus._EXIST_ENTITY;

@Service
@RequiredArgsConstructor
public class ExpertInfoUseCase {

    private final ExpertService expertService;
    private final BadWordFilterUtil badWordFilterUtil;
    private final UserService userService;

    @Transactional
    public Expert save(Long userNo, RegExpReq request) {
        if (expertService.isExist(userNo))
            throw new RestApiException(_EXIST_ENTITY);

        filterBadWord(request);

        Expert expert = expertService.save(userNo, request);
        User user = userService.findById(userNo);
        user.updateUserType(UserType.EXPERT);

        return expert;
    }

    private void filterBadWord(RegExpReq request) {
        badWordFilterUtil.filterBadWord(request.intro());
        badWordFilterUtil.filterBadWord(request.career());
        request.skills().iterator().forEachRemaining(badWordFilterUtil::filterBadWord);
    }
}
