package com.picus.core.domain.studio.entity;

import com.picus.core.global.common.enums.ApprovalStatus;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Studio {

    @Id @Tsid
    @Column(name = "studio_no")
    private Long id;

    @Column(length = 20)
    private String name;

    private String backgroundImgUrl;

    private Address address;

    private Integer reviewCount;

    private Integer activityCount;

    private Double avgRating;

    private LocalDateTime recentActiveAt;

    @Column(nullable = false, name = "expert_no")
    private Long expertNo;

    private ApprovalStatus approvalStatus;

    public Studio(String name, String backgroundImgUrl, Address address, Long expertNo) {
        this.name = name;
        this.backgroundImgUrl = backgroundImgUrl;
        this.address = address;
        this.reviewCount = 0;
        this.activityCount = 0;
        this.avgRating = 0.0;
        this.recentActiveAt = LocalDateTime.now();
        this.expertNo = expertNo;
        this.approvalStatus = ApprovalStatus.PENDING;
    }
}
