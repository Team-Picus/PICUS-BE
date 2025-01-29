package com.picus.core.domain.post.entity.category;

import com.picus.core.global.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@IdClass(PostCategoryId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCategory extends BaseEntity {

    @Id
    private Long postNo;

    @Id
    private Long categoryId;

    public PostCategory(Long postNo, Long categoryId) {
        this.postNo = postNo;
        this.categoryId = categoryId;
    }
}
