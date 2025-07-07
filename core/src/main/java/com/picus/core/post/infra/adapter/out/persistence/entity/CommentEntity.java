package com.picus.core.post.infra.adapter.out.persistence.entity;

import com.picus.core.shared.common.BaseEntity;
import com.picus.core.user.infra.adapter.out.persistence.entity.UserEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentEntity extends BaseEntity {

    @Id @Tsid
    private String commentNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_no")
    private PostEntity postEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_no")
    private UserEntity userEntity;

    @Column(nullable = false)
    private String content;
}
