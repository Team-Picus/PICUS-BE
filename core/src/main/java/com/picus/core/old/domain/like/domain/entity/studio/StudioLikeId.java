package com.picus.core.old.domain.like.domain.entity.studio;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StudioLikeId {

    @EqualsAndHashCode.Include
    private Long userNo;

    @EqualsAndHashCode.Include
    private Long studioNo;

    public StudioLikeId(Long userNo, Long studioNo) {
        this.userNo = userNo;
        this.studioNo = studioNo;
    }
}
