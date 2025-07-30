package com.picus.core.moodboard.adapter.out.persistence.entity;

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
    private String userNo;

    @Id
    private String postNo;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void init() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}