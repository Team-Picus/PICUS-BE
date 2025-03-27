package com.picus.core.domain.like.domain.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Like {

    @Id @Tsid
    @Column(name = "like_no")
    private Long id;

    @Column(nullable = false)
    private Long userNo;

    private LocalDateTime createdAt;

    protected Like(Long userNo) {
        this.userNo = userNo;
        this.createdAt = LocalDateTime.now();
    }
}
