package com.picus.core.old.domain.review.entity.image;

import com.picus.core.old.domain.shared.image.domain.entity.Image;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImageResource extends Image {

    @Column(nullable = false)
    private Long reviewNo;

    public ReviewImageResource(String preSignedKey, Long reviewNo) {
        super(preSignedKey);
        this.reviewNo = reviewNo;
    }
}
