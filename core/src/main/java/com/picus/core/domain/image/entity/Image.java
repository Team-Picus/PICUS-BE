package com.picus.core.domain.image.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Image {   // 사진 구현 시 추가 수정

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private String extension;

    private LocalDateTime uploadedAt;

    public Image(String path, String extension) {
        this.path = path;
        this.extension = extension;
        this.uploadedAt = LocalDateTime.now();
    }
}
