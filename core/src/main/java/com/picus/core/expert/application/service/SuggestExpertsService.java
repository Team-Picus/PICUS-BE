package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.SuggestExpertsUseCase;
import com.picus.core.expert.application.port.in.response.SuggestExpertResult;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.application.port.out.join_dto.UserWithProfileImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SuggestExpertsService implements SuggestExpertsUseCase {

    private final UserReadPort userReadPort;

    @Override
    public List<SuggestExpertResult> suggestExperts(String keyword, int size) {
        // 해당 Keyword를 닉네임으로 가진 Expert를 size 갯수만큼 조회
        List<UserWithProfileImageDto> dtos = userReadPort.findTopNUserInfoByNicknameContainingOrderByNickname(keyword, size);

        // 변환 및 반환
        return dtos.stream()
                .map(userWithProfileImageDto -> SuggestExpertResult.builder()
                        .expertNo(userWithProfileImageDto.expertNo())
                        .nickname(userWithProfileImageDto.nickname()) // TODO: image key -> image url
                        .profileImageUrl("")
                        .build()
                ).toList();
    }
}
