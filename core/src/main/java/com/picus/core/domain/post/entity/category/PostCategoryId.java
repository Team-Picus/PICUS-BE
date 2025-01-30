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
    private Long post;

    @EqualsAndHashCode.Include
    private Long category;

    public PostCategoryId(Long post, Long category) {
        this.post = post;
        this.category = category;
    }
}
