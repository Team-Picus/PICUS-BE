package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.SuggestExpertsQuery;
import com.picus.core.expert.application.port.in.response.SuggestExpertAppResponse;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.out.ReadUserPort;
import com.picus.core.user.application.port.out.join_dto.UserWithProfileImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SuggestExpertsService implements SuggestExpertsQuery {

    private final ReadUserPort readUserPort;

    @Override
    public List<SuggestExpertAppResponse> suggestExperts(String keyword, int size) {
        // 해당 Keyword를 닉네임으로 가진 Expert를 size 갯수만큼 조회
        List<UserWithProfileImageDto> dtos = readUserPort.findUserInfoByNicknameContainingLimited(keyword, size);

        // 변환 및 반환
        return dtos.stream()
                .map(userWithProfileImageDto -> SuggestExpertAppResponse.builder()
                        .expertNo(userWithProfileImageDto.expertNo())
                        .nickname(userWithProfileImageDto.nickname()) // TODO: image key -> image url
                        .profileImageUrl("")
                        .build()
                ).toList();
    }
}
