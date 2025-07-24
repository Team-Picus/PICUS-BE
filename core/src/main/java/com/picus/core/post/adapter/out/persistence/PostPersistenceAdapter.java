package com.picus.core.post.adapter.out.persistence;

import com.picus.core.post.adapter.out.persistence.repository.PostJpaRepository;
import com.picus.core.post.application.port.out.PostCommandPort;
import com.picus.core.post.domain.model.Post;
import com.picus.core.shared.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class PostPersistenceAdapter implements PostCommandPort {

    private final PostJpaRepository postJpaRepository;

    @Override
    public void save(Post post) {

    }
}
