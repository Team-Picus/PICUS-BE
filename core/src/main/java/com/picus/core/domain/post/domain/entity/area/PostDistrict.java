package com.picus.core.domain.post.domain.entity.area;

import com.picus.core.domain.post.domain.entity.Post;
import com.picus.core.global.common.BaseEntity;
import com.picus.core.global.common.area.entity.District;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(PostDistrictId.class)
public class PostDistrict extends BaseEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "post_no", nullable = false)
    private Post post;

    @Id
    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    public PostDistrict(Post post, District district) {
        this.post = post;
        this.district = district;
    }
}
