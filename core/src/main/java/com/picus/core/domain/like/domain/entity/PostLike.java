package com.picus.core.domain.like.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike extends Like {

    @Column(nullable = false)
    private Long postNo;

    public PostLike(Long user_no, Long postNo) {
        super(user_no);
        this.postNo = postNo;
    }
}
