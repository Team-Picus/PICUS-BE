package com.picus.core.follow.adapter.out.persistence.entity;

import com.picus.core.expert.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import jakarta.persistence.*;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private UserEntity user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_no", nullable = false)
    private ExpertEntity expert;

    @Column(nullable = false)
    private LocalDateTime followedAt;

    @PrePersist
    protected void init() {
        if (this.followedAt == null) {
            this.followedAt = LocalDateTime.now();
        }
    }
}