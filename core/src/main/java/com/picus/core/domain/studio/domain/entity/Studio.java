package com.picus.core.domain.studio.domain.entity;

import com.picus.core.global.common.base.BaseEntity;
import com.picus.core.domain.shared.enums.ApprovalStatus;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Studio extends BaseEntity {

    @Id @Tsid
    @Column(name = "studio_no")
    private Long id;

    @Column(length = 20)
    private String name;

    private String backgroundImgUrl; // 스튜디오 대표 이미지

    private Address address; // 스튜디오 주소

//    private Integer reviewCount;
//    private Integer activityCount;
//    private Double avgRating;

    private LocalDateTime recentActiveAt;

    @Column(nullable = false, name = "expert_no")
    private Long expertNo;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    // ======================================
    // =            Constructors            =
    // ======================================
    public Studio(String name, String backgroundImgUrl, Address address, Long expertNo) {
        this.name = name;
        this.backgroundImgUrl = backgroundImgUrl;
        this.address = address;
//        this.reviewCount = 0;
//        this.activityCount = 0;
//        this.avgRating = 0.0;
        this.recentActiveAt = LocalDateTime.now();
        this.expertNo = expertNo;
        this.approvalStatus = ApprovalStatus.PENDING;
    }
}
