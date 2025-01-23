package com.picus.core.domain.image.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage extends Image {

    @Column(nullable = false)
    private Long reviewNo;

    public ReviewImage(String path, String extension, Long reviewNo) {
        super(path, extension);
        this.reviewNo = reviewNo;
    }
}
