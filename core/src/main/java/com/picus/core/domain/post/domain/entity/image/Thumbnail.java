package com.picus.core.domain.post.domain.entity.image;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Thumbnail {

    @Column(name = "thumbnail_path", nullable = false)
    private String path;

    @Column(name = "thumbnail_extension", nullable = false)
    private String extension;

    public Thumbnail(String path, String extension) {
        this.path = path;
        this.extension = extension;
    }
}
