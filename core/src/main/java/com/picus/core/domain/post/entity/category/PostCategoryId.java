package com.picus.core.domain.post.entity.category;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostCategoryId implements Serializable {

    @EqualsAndHashCode.Include
    private Long postNo;

    @EqualsAndHashCode.Include
    private Long categoryId;

    public PostCategoryId(Long postNo, Long categoryId) {
        this.postNo = postNo;
        this.categoryId = categoryId;
    }
}
