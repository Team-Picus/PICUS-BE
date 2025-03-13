package com.picus.core.global.common.image.domain.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Image {

    @Id @Tsid
    @Column(name = "image_id")
    private Long id;

    @Column(nullable = false)
    private String preSignedKey;

    private LocalDateTime uploadedAt;

    protected Image(String preSignedKey) {
        this.preSignedKey = preSignedKey;
        this.uploadedAt = LocalDateTime.now();
    }
}
