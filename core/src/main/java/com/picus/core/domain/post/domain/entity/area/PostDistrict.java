package com.picus.core.domain.post.domain.entity.area;

import com.picus.core.domain.post.domain.entity.Post;
import com.picus.core.global.common.area.entity.District;
import com.picus.core.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@IdClass(PostDistrictId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDistrict extends BaseEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_no", nullable = false)
    private Post post;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "district", nullable = false)
    private District district;

    public PostDistrict(Post post, District district) {
        this.post = post;
        this.district = district;
    }
}
