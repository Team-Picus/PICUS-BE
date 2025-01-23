package com.picus.core.domain.like.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudioLike extends Like {

    @Column(nullable = false)
    private Long studioNo;

    public StudioLike(Long user_no, Long studioNo) {
        super(user_no);
        this.studioNo = studioNo;
    }
}
