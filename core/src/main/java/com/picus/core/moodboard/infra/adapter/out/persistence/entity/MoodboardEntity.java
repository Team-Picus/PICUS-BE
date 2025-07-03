package com.picus.core.moodboard.infra.adapter.out.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.io.Serializable;
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

    @PastOrPresent(message = "createdAt은 과거 또는 현재 시각이어야 합니다.")
    private LocalDateTime createdAt;

    @PrePersist
    protected void init() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class MoodboardId implements Serializable {

    @EqualsAndHashCode.Include
    private Long userNo;

    @EqualsAndHashCode.Include
    private Long postNo;

    public MoodboardId(Long userNo, Long postNo) {
        this.userNo = userNo;
        this.postNo = postNo;
    }
}