package com.picus.core.expert.infra.adapter.out.persistence.entity;

import com.picus.core.expert.domain.model.vo.ActivityArea;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import com.picus.core.expert.infra.adapter.out.persistence.converter.ActivityAreasConverter;
import com.picus.core.expert.infra.adapter.out.persistence.converter.StringConverter;
import com.picus.core.shared.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;
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

    private String backgroundImageKey;

    private String intro;

    @Column(nullable = false)
    private String activityCareer;

    @Convert(converter = ActivityAreasConverter.class)
    @Column(nullable = false)
    private List<ActivityArea> activityAreas;

    @Column(nullable = false)
    private String activityDuration;

    @Column(nullable = false)
    private Integer activityCount;

    @Column(nullable = false)
    private LocalDateTime recentlyActivityAt;

    @Convert(converter = StringConverter.class)
    private List<String> portfolioLinks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus approvalStatus;

}
