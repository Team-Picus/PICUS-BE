package com.picus.core.domain.post.domain.entity.image;

import com.picus.core.global.common.image.domain.entity.Image;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImageResource extends Image {

    @Column(nullable = false)
    private Long postNo;

    public PostImageResource(String preSignedKey, Long postNo) {
        super(preSignedKey);
        this.postNo = postNo;
    }
}
