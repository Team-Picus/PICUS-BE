package com.picus.core.domain.post.domain.entity.cateogory;


import com.picus.core.domain.shared.category.entity.Category;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostCategoryId implements Serializable {

    @EqualsAndHashCode.Include
    private Long post; // Post 엔티티의 ID

    @EqualsAndHashCode.Include
    private Category category; // Category enum 값

    public PostCategoryId(Long post, Category category) {
        this.post = post;
        this.category = category;
    }
}