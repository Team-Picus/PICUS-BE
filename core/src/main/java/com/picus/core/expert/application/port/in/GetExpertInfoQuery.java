package com.picus.core.expert.application.port.in;

import com.picus.core.expert.domain.model.Expert;

public interface GetExpertInfoQuery {

    Expert getExpertInfo(String expertNo);

}
