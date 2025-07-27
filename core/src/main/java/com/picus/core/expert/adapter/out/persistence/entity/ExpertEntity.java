package com.picus.core.expert.adapter.out.persistence.entity;

import com.picus.core.expert.domain.Expert;
import com.picus.core.expert.domain.vo.ApprovalStatus;
import com.picus.core.expert.adapter.out.persistence.converter.StringConverter;
import com.picus.core.expert.domain.vo.Portfolio;
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
        if (newExpert.getLastActivityAt() != null) {
            this.lastActivityAt = newExpert.getLastActivityAt();
        }
        if (newExpert.getPortfolios() != null) {
            this.portfolioLinks = newExpert.getPortfolios().stream()
                    .map(Portfolio::getLink)
                    .toList();
        }
        if (newExpert.getApprovalStatus() != null) {
            this.approvalStatus = newExpert.getApprovalStatus();
        }
    }

    public void bindUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

}
