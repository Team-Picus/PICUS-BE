package com.picus.core.expert.application.port.out;

import com.picus.core.expert.domain.Expert;
/**
 * Expert를 수정하는 Out Port
 */
public interface ExpertCreatePort {

    Expert create(Expert expert, String userNo);
}
