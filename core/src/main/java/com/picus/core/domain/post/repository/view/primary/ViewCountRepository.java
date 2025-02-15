package com.picus.core.domain.post.repository.view.primary;

import com.picus.core.domain.post.entity.view.ViewCount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewCountRepository extends CrudRepository<ViewCount, String>, ViewCountCustomRepository {
}
