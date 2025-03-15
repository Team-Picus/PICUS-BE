package com.picus.core.domain.review.entity;

import com.picus.core.global.common.base.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id @Tsid
    @Column(name = "review_no")
    private Long id;

    private String content;

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private Integer rating; // 별점

    @Column(nullable = false)
    private Long reservationNo;

    @Column(nullable = false)
    private Long postNo;

    @Column(nullable = false)
    private Long userNo;


    public Review(String content, Integer rating, Long reservationNo, Long postNo, Long userNo) {
        this.content = content;
        this.rating = rating;
        this.reservationNo = reservationNo;
        this.postNo = postNo;
        this.userNo = userNo;
    }

}
