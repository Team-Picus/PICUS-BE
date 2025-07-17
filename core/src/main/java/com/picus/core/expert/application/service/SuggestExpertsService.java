package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.SuggestExpertsQuery;
import com.picus.core.expert.application.port.in.response.SearchExpertAppResponse;
import com.picus.core.expert.application.port.in.response.SuggestExpertAppResponse;
import com.picus.core.expert.application.port.out.LoadExpertPort;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.ProfileImage;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SuggestExpertsService implements SuggestExpertsQuery {

    private final LoadExpertPort loadExpertPort;
    private final UserQueryPort userQueryPort;

    @Override
    public List<SuggestExpertAppResponse> suggestExperts(String keyword, int size) {

        // 해당 keyword가 포함되는 닉네임을 가진 전문가 닉네임 순으로 size 수만큼 조회
        List<SuggestExpertAppResponse> suggestExpertAppResponses =
                loadExpertPort.findByNicknameContainingLimited(keyword, size);

        // 프로필 이미지 조회 및 응답 반환
        return suggestExpertAppResponses.stream()
                .map(suggestExpertAppResponse -> {
                    ProfileImage profileImage = userQueryPort.findProfileImageByExpertNo(suggestExpertAppResponse.expertNo());
                    return SuggestExpertAppResponse.builder()
                            .profileImageUrl("") // TODO: image key -> image url
                            .expertNo(suggestExpertAppResponse.expertNo())
                            .nickname(suggestExpertAppResponse.nickname())
                            .build();
                }).toList();
    }
}
