package com.picus.core.expert.adapter.out.persistence.entity;

import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.vo.ActivityArea;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import com.picus.core.expert.adapter.out.persistence.converter.ActivityAreasConverter;
import com.picus.core.expert.adapter.out.persistence.converter.StringConverter;
import com.picus.core.expert.domain.model.vo.Portfolio;
import com.picus.core.shared.common.BaseEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@SuperBuilder
@Entity
@Table(name = "experts")
@NoArgsConstructor(access = PROTECTED)
public class ExpertEntity extends BaseEntity {

    @Id
    @Tsid
    private String expertNo;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "expert_no")  // User와 PK 공유
    private UserEntity user;

    private String backgroundImageKey;

    private String intro;

    @Column(nullable = false)
    private String activityCareer;

    @Convert(converter = ActivityAreasConverter.class)
    @Column(nullable = false)
    private List<ActivityArea> activityAreas;

    @Column(nullable = false)
    private Integer activityCount;

    private LocalDateTime lastActivityAt;

    @Convert(converter = StringConverter.class)
    private List<String> portfolioLinks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus approvalStatus;

    public void updateEntity(Expert newExpert) {
        this.backgroundImageKey = newExpert.getBackgroundImageKey();
        this.intro = newExpert.getIntro();
        this.activityCareer = newExpert.getActivityCareer();
        this.activityAreas = newExpert.getActivityAreas();
        this.activityCount = newExpert.getActivityCount();
        this.lastActivityAt = newExpert.getLastActivityAt();
        this.portfolioLinks = newExpert.getPortfolios().stream()
                .map(Portfolio::getLink).toList();
        this.approvalStatus = newExpert.getApprovalStatus();
    }

}
