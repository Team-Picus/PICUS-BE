package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.SearchExpertsUseCase;
import com.picus.core.expert.application.port.in.response.SearchExpertResult;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.application.port.out.join_dto.UserWithProfileImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchExpertsService implements SearchExpertsUseCase {
    private final UserQueryPort userQueryPort;

    @Override
    public List<SearchExpertResult> searchExperts(String keyword) {
        // 해당 Keyword를 닉네임으로 가진 Expert를 조회
        List<UserWithProfileImageDto> dtos = userQueryPort.findUserInfoByNicknameContaining(keyword);

        // 변환 및 반환
        return dtos.stream()
                .map(userWithProfileImageDto -> SearchExpertResult.builder()
                        .expertNo(userWithProfileImageDto.expertNo())
                        .nickname(userWithProfileImageDto.nickname()) // TODO: image key -> image url
                        .profileImageUrl("")
                        .build()
                ).toList();

    }
}
