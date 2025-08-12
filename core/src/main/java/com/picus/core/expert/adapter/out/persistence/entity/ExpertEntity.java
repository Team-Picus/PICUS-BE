package com.picus.core.expert.adapter.out.persistence.entity;

import com.picus.core.expert.domain.Expert;
import com.picus.core.expert.domain.vo.ApprovalStatus;
import com.picus.core.expert.adapter.out.persistence.converter.StringConverter;
import com.picus.core.shared.common.BaseEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
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
    private String expertNo;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "expert_no")  // User와 PK 공유
    private UserEntity userEntity;

    private String backgroundImageKey;

    private String intro;

    @Column(nullable = false)
    private String activityCareer;

    @Convert(converter = StringConverter.class)
    @Column(nullable = false)
    private List<String> activityAreas;

    @Column(nullable = false)
    private Integer activityCount;

    private LocalDateTime lastActivityAt;

    @Convert(converter = StringConverter.class)
    private List<String> portfolioLinks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus approvalStatus;

    public void updateEntity(Expert newExpert) {
        if (newExpert.getBackgroundImageKey() != null) {
            this.backgroundImageKey = newExpert.getBackgroundImageKey();
        }
        if (newExpert.getIntro() != null) {
            this.intro = newExpert.getIntro();
        }
        if (newExpert.getActivityCareer() != null) {
            this.activityCareer = newExpert.getActivityCareer();
        }
        if (newExpert.getActivityAreas() != null) {
            this.activityAreas = newExpert.getActivityAreas();
        }
        if (newExpert.getActivityCount() != null) {
            this.activityCount = newExpert.getActivityCount();
        }

        this.lastActivityAt = newExpert.getLastActivityAt(); // lastActivityAt은 다시 null이 들어가야할 상황이 있음

        if (newExpert.getPortfolioLinks() != null) {
            this.portfolioLinks = newExpert.getPortfolioLinks();
        }
        if (newExpert.getApprovalStatus() != null) {
            this.approvalStatus = newExpert.getApprovalStatus();
        }
    }

    public void bindUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

}
