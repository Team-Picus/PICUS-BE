package com.picus.core.domain.post.entity;

import com.picus.core.global.common.enums.ApprovalStatus;
import com.picus.core.global.common.enums.Area;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @Tsid
    @Column(name = "post_no")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String detail;


    private Integer reviewCount;

    private Integer likeCount;

    @Column(nullable = false)
    private Long studioNo;

    @Column(nullable = false)
    private Set<Area> activeAreas = new HashSet<>();

    private ApprovalStatus approvalStatus;

    public Post(String title, String detail, Long studioNo, Set<Area> activeAreas) {
        this.title = title;
        this.detail = detail;
        this.reviewCount = 0;
        this.likeCount = 0;
        this.studioNo = studioNo;
        this.activeAreas = activeAreas;
        this.approvalStatus = ApprovalStatus.PENDING;
    }
}
