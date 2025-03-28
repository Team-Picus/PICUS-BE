package com.picus.core.domain.like.domain.entity.studio;

import com.picus.core.global.common.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@IdClass(StudioLikeId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudioLike extends BaseEntity {

    @Id
    private Long userNo;

    @Id
    private Long studioNo;

    public StudioLike(Long userNo, Long studioNo) {
        this.userNo = userNo;
        this.studioNo = studioNo;
    }
}
