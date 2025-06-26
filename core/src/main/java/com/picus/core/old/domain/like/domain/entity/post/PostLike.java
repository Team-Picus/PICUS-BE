package com.picus.core.old.domain.like.domain.entity.post;

import com.picus.core.old.global.common.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@IdClass(PostLikeId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike extends BaseEntity {

    @Id
    private Long userNo;

    @Id
    private Long postNo;

    public PostLike(Long userNo, Long postNo) {
        this.userNo = userNo;
        this.postNo = postNo;
    }
}
