package com.picus.core.follow.infra.adapter.out.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.io.Serializable;
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
    private String followerNo;

    @Id
    private String followeeNo;

    @Column(nullable = false)
    private LocalDateTime followedAt;

    @PrePersist
    protected void init() {
        if (this.followedAt == null) {
            this.followedAt = LocalDateTime.now();
        }
    }
}

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class FollowId implements Serializable {

    @EqualsAndHashCode.Include
    private Long followerNo;

    @EqualsAndHashCode.Include
    private Long followeeNo;

    public FollowId(Long followerNo, Long followeeNo) {
        this.followerNo = followerNo;
        this.followeeNo = followeeNo;
    }
}
