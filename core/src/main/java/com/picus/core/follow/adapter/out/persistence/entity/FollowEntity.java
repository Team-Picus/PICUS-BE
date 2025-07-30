package com.picus.core.follow.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@IdClass(FollowId.class)
@Table(name = "follow")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FollowEntity {

    @Id
    private String userNo;

    @Id
    private String expertNo;

    @Column(nullable = false)
    private LocalDateTime followedAt;

    @PrePersist
    protected void init() {
        if (this.followedAt == null) {
            this.followedAt = LocalDateTime.now();
        }
    }
}