package com.picus.core.expert.application.port.in;

import com.picus.core.expert.application.port.in.response.SearchExpertResponse;

import java.util.List;

public interface SearchExpertQuery {
    List<SearchExpertResponse> searchExpert(String keyword);
}
