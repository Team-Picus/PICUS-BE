package com.picus.core.expert.application.port.out;

import com.picus.core.expert.domain.model.Expert;

import java.util.Optional;

/**
 * 전문가를 데이터베이스로부터 불러오는 Out Port
 */
public interface ExpertQueryPort {

    Optional<Expert> findById(String expertNo);
}
