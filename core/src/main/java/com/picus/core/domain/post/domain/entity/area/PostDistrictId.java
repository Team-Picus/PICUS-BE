package com.picus.core.domain.post.domain.entity.area;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostDistrictId implements Serializable {

    @EqualsAndHashCode.Include
    private Long post;

    @EqualsAndHashCode.Include
    private Long district;

    public PostDistrictId(Long post, Long district) {
        this.post = post;
        this.district = district;
    }
}
