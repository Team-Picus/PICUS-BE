package com.picus.core.expert.application.port.in;

import com.picus.core.expert.application.port.in.result.SuggestExpertResult;

import java.util.List;

/**
 * 전문가 검색 중 특정 키워드가 포함되는 닉네임을 가진 전문가를 특정 수만큼 뽑아서 검색어 추천
 */
public interface SuggestExpertsUseCase {


    List<SuggestExpertResult> suggestExperts(String keyword, int size);
}
