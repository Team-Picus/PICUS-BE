package com.picus.core.post.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Entity
@Table(name = "post_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostImageEntity {

    @Id @Tsid
    private String postImageNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_no")
    private PostEntity postEntity;

    @Column(nullable = false)
    private String fileKey;

    @Column(nullable = false)
    private Integer imageOrder;

    public void bindPostEntity(PostEntity postEntity) {
        this.postEntity = postEntity;
    }
}
