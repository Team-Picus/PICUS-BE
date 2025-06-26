package com.picus.core.old.domain.like.domain.entity.post;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostLikeId implements Serializable {

    @EqualsAndHashCode.Include
    private Long userNo;

    @EqualsAndHashCode.Include
    private Long postNo;

    public PostLikeId(Long userNo, Long postNo) {
        this.userNo = userNo;
        this.postNo = postNo;
    }
}
