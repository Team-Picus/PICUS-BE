package com.picus.core.domain.post.repository.view.primary;

import java.util.Optional;

public interface ViewCountCustomRepository {

    void increment(String key);

    Optional<Integer> findViewCount(String key);
}
