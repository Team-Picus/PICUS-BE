package com.picus.core.post.adapter.out.persistence.entity;

import com.picus.core.shared.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Getter
public class CommentEntity extends BaseEntity {

    @Id @Tsid
    private String commentNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_no")
    private PostEntity postEntity;

    @Column(nullable = false)
    private String userNo;

    @Column(nullable = false)
    private String content;

    public void bindPostEntity(PostEntity postEntity) {
        this.postEntity = postEntity;
    }
}
