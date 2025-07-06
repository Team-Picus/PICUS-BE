package com.picus.core.expert.infra.adapter.out.persistence.entity;

import com.picus.core.expert.domain.model.vo.ActivityArea;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import com.picus.core.expert.infra.adapter.out.persistence.converter.ActivityAreasConverter;
import com.picus.core.expert.infra.adapter.out.persistence.converter.StringConverter;
import com.picus.core.old.global.common.base.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "experts")
public class ExpertEntity extends BaseEntity {

    @Id @Tsid
    private String expertNo;

    private String backgroundImageKey;
    private String intro;
    private String career;
    @Convert(converter = ActivityAreasConverter.class)
    private List<ActivityArea> activityAreas = new ArrayList<>();
    private String activityDuration;
    private int activityCount;
    private LocalDateTime recentlyActivityAt;
    @Convert(converter = StringConverter.class)
    private List<String> portfolioLinks = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    // 스튜디오 정보
    private String studioName;
    private Integer employeesCount;
    private String businessHours;
    private String address;

}
