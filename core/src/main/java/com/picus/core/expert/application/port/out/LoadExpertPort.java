package com.picus.core.expert.application.port.out;

import com.picus.core.expert.application.port.in.response.SearchExpertAppResponse;
import com.picus.core.expert.application.port.in.response.SuggestExpertAppResponse;
import com.picus.core.expert.domain.model.Expert;

import java.util.List;
import java.util.Optional;

/**
 * 전문가를 데이터베이스로부터 불러오는 Out Port
 */
public interface LoadExpertPort {

    Optional<Expert> findById(String expertNo);

    List<SearchExpertAppResponse> findByNicknameContaining(String keyword);

    List<SuggestExpertAppResponse> findByNicknameContainingLimited(String keyword, int size);
}
