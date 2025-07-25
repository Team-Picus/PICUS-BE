package com.picus.core.moodboard.adapter.out.persistence.entity;

import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@Table(name = "moodboard")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@IdClass(MoodboardId.class)
public class MoodboardEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private UserEntity user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_no", nullable = false)
    private PostEntity post;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void init() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}