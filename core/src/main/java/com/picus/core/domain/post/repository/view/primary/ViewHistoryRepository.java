package com.picus.core.domain.post.repository.view.primary;

import com.picus.core.domain.post.entity.view.ViewHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewHistoryRepository extends CrudRepository<ViewHistory, String>, ViewHistoryCustomRepository {
}
