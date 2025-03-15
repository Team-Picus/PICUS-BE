package com.picus.core.domain.post.domain.entity;

import com.picus.core.domain.post.domain.entity.pricing.BasicOption;
import com.picus.core.domain.post.domain.entity.statistic.PostStatistics;
import com.picus.core.global.common.area.entity.District;
import com.picus.core.global.common.base.BaseEntity;
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
public class Post extends BaseEntity {

    @Id
    @Tsid
    @Column(name = "post_no")
    private Long id;

    // 기본 정보
    private String title;
    private String detail;
    private Long studioNo;

    // 가용 지역
    @ElementCollection(targetClass = District.class)
    @CollectionTable(name = "post_district", joinColumns = @JoinColumn(name = "post_no"))
    @Column(name = "district")
    @Enumerated(EnumType.STRING)
    private List<District> availableAreas = new ArrayList<>();

    // 옵션 정보
    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private BasicOption basicOption;

    // 상태
    private PostStatus postStatus = PostStatus.DRAFT;

    // 승인 상태
    private ApprovalStatus approvalStatus;

    // 통계성
    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private PostStatistics statistics;




    // ======================================
    // =            Constructors            =
    // ======================================
    public Post(Long studioNo) {
        this.studioNo = studioNo;
        this.approvalStatus = ApprovalStatus.PENDING;
        this.statistics = new PostStatistics(this);
    }

    // ======================================
    // =          Business methods          =
    // ======================================

    public boolean initialize(String title,
                              String detail,
                              List<District> availableAreas,
                              Integer basicPrice) {

        // 이미 생성된 경우
        if (getPostStatus() != PostStatus.DRAFT) {
            // TODO 예외 정의 후 예외 던져야한다.
            return false;
        }

        // 가용 지역이 없는 경우
        if (availableAreas.isEmpty()) {
            // TODO 예외 정의 후 예외 던져야한다.
            return false;
        }

        this.title = title;
        this.detail = detail;
        this.availableAreas = availableAreas;
        this.postStatus = PostStatus.PUBLISHED;
        this.basicOption = new BasicOption(this, basicPrice);

        return true;
    }

}
