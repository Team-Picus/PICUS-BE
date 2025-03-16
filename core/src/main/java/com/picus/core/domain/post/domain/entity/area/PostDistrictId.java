package com.picus.core.domain.post.domain.entity.area;

import com.picus.core.global.common.area.entity.District;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostDistrictId implements Serializable {

    @EqualsAndHashCode.Include
    private Long post;  // Post 엔티티의 ID

    @EqualsAndHashCode.Include
    private District district;  // District enum 값

    public PostDistrictId(Long postId, District district) {
        this.post = postId;
        this.district = district;
    }
}