package com.picus.core.domain.post.domain.entity.cateogory;

import com.picus.core.domain.post.domain.entity.Post;
import com.picus.core.global.common.category.entity.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@IdClass(PostCategoryId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCategory {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_no", nullable = false)
    private Post post;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    public PostCategory(Post post, Category category) {
        this.post = post;
        this.category = category;
    }
}