package com.picus.core.domain.review.entity;

import com.picus.core.domain.shared.image.domain.entity.Image;
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
