package com.picus.core.expert.application.port.in;

import com.picus.core.expert.application.port.in.response.SearchExpertAppResp;

import java.util.List;

/**
 * 특정 키워드가 포함되는 닉네임을 가진 전문가를 검색한다.
 */
public interface SearchExpertsUseCase {
    List<SearchExpertAppResp> searchExperts(String keyword);
}
