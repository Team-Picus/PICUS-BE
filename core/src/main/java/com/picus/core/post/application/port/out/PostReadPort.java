package com.picus.core.post.application.port.out;

import com.picus.core.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostReadPort {
    Optional<Post> findById(String postNo);

    Optional<LocalDateTime> findTopUpdatedAtByExpertNo(String expertNo);

    Optional<Post> findByExpertNoAndIsPinnedTrue(String expertNo);

    /**
     제목에 특정 키워드가 포함된 게시물을 제목을 기준으로 정렬해서 N개 가져옴
     정렬 기준:
        1. 검색한 키워드로 시작하는 결과
        2. 검색한 키워드가 포함된 결과
        (내부적으로는 가나다순)
     */
    List<Post> findTopNByTitleContainingOrderByTitle(String keyword, int size);

    List<Post> findRandomTopN(int size);
}
