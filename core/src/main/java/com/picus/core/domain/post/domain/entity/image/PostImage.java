package com.picus.core.domain.post.domain.entity.image;

import com.picus.core.global.common.image.entity.Image;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage extends Image {

    @Column(nullable = false)
    private String postNo;

    public PostImage(String path, String extension, String postNo) {
        super(path, extension);
        this.postNo = postNo;
    }
}
