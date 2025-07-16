package com.picus.core.expert.application.port.out;

import com.picus.core.expert.domain.model.Expert;

public interface SaveExpertPort {

    Expert saveExpert(Expert expert, String userNo);
}
