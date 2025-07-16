package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.SearchExpertQuery;
import com.picus.core.expert.application.port.in.response.SearchExpertResponse;
import com.picus.core.expert.application.port.out.LoadExpertPort;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.ProfileImage;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class SearchExpertService implements SearchExpertQuery {

    private final LoadExpertPort loadExpertPort;
    private final UserQueryPort userQueryPort;

    @Override
    public List<SearchExpertResponse> searchExpert(String keyword) {
        // 해당 Keyword를 닉네임으로 가진 Expert를 조회
        List<SearchExpertResponse> searchExpertResponses = loadExpertPort.findByNicknameContaining(keyword);

        // 프로필 이미지 조회 및 응답 반환
        return searchExpertResponses.stream()
                .map(searchExpertResponse -> {
                            ProfileImage profileImage = userQueryPort.findProfileImageByExpertNo(searchExpertResponse.expertNo());
                            return SearchExpertResponse.builder()
                                    .profileImageUrl("") // TODO: image key -> image url
                                    .expertNo(searchExpertResponse.expertNo())
                                    .nickname(searchExpertResponse.nickname())
                                    .build();
                        }
                ).toList();
    }
}
