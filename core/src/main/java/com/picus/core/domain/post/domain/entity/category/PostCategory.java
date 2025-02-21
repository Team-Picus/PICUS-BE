package com.picus.core.domain.post.domain.entity.category;

import com.picus.core.domain.post.domain.entity.Post;
import com.picus.core.global.common.BaseEntity;
import com.picus.core.global.common.category.entity.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@IdClass(PostCategoryId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCategory extends BaseEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "post_no", nullable = false)
    private Post post;

    @Id
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public PostCategory(Post post, Category category) {
        this.post = post;
        this.category = category;
    }
}
