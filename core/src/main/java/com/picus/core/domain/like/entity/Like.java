package com.picus.core.domain.like.entity;

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
    private Long user_no;

    private LocalDateTime createdAt;

    protected Like(Long user_no) {
        this.user_no = user_no;
        this.createdAt = LocalDateTime.now();
    }
}
