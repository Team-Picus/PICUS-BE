package com.picus.core.domain.post.domain.entity.statistic;

import com.picus.core.domain.post.domain.entity.Post;
import com.picus.core.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostStatistics extends BaseEntity {

    @Id
    @Column(name = "post_no")
    private Long postId;

    // 양수이어야한다.
    private Integer reviewCount = 0;
    private Integer likeCount = 0;
    private Integer viewCount = 0;

    @OneToOne
    @MapsId
    @JoinColumn(name = "post_no")
    private Post post;

    public PostStatistics(Post post) {
        this.post = post;
    }

    // ======================================
    // =          Business methods          =
    // ======================================
    public void increaseReviewCount() {
        this.reviewCount++;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
}
