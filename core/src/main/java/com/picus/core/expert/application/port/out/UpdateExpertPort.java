package com.picus.core.expert.application.port.out;

import com.picus.core.expert.domain.model.Expert;

/**
 * Expert를 수정하는 Out Port
 */
public interface UpdateExpertPort {

    void updateExpert(Expert expert);
}
