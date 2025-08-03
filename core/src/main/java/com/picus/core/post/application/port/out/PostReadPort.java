package com.picus.core.post.application.port.out;

import com.picus.core.post.adapter.out.persistence.dto.SearchPostCond;
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

    /**
     * 고정처리된 게시물(갤러리)을 랜덤으로 N개 가져옴
     */
    List<Post> findRandomTopNByIsPinnedTrue(int size);

    /**
     * @param cond: 게시물 검색 조건
     * @param cursor: 커서(마지막으로 조회한 게시물)
     * @param sortBy: 정렬 기준
     * @param sortDirection: 정렬 방향
     * @param size: 조회 갯수
     * @return: 검색 결과
     */
    List<Post> findBySearchCond(SearchPostCond cond, String cursor, String sortBy, String sortDirection, int size);
}
