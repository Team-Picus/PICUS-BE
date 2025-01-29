package com.picus.core.domain.post.entity;

import com.picus.core.domain.post.entity.area.PostDistrict;
import com.picus.core.global.common.enums.ApprovalStatus;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @Tsid
    @Column(name = "post_no")
    private Long id;

    // 기본 정보
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String detail;

    @Column(nullable = false)
    private Long studioNo;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostDistrict> availableAreas = new ArrayList<>();

    // 승인 상태
    private ApprovalStatus approvalStatus;

    // 통계셩
    private Integer reviewCount;

    private Integer likeCount;


    public Post(String title, String detail, Long studioNo) {
        this.title = title;
        this.detail = detail;
        this.reviewCount = 0;
        this.likeCount = 0;
        this.studioNo = studioNo;
        this.approvalStatus = ApprovalStatus.PENDING;
    }

    public void updateAvailableAreas(List<PostDistrict> areas) {
        for (PostDistrict area : areas) {
            if (!this.availableAreas.contains(area)) {
                this.availableAreas.add(area);
            }
        }

        this.availableAreas.removeIf(existingArea -> !areas.contains(existingArea));
    }
}
