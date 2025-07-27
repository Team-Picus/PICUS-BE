package com.picus.core.expert.application.port.out;

import com.picus.core.expert.domain.Expert;

import java.util.List;

/**
 * Expert를 수정하는 Out Port
 */
public interface UpdateExpertPort {
    /**
     * Expert 도메인만 수정
     */
    void update(Expert expert);

    /**
     * Expert의 서브도메인인 Project, Skill, Studio까지 업데이트
     */
    void update(Expert expert,
                List<String> deletedProjectNos, List<String> deletedSkillNos, String deletedStudioNo);
}
