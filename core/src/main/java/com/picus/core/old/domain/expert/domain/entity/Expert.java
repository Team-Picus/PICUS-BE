package com.picus.core.old.domain.expert.domain.entity;

import com.picus.core.old.domain.expert.application.dto.request.RegExpReq;
import com.picus.core.old.domain.shared.area.District;
import com.picus.core.old.global.common.base.BaseEntity;
import com.picus.core.old.domain.shared.approval.ApprovalStatus;
import com.picus.core.old.global.common.converter.ActivityTypeSetConverter;
import com.picus.core.old.global.common.converter.DistrictSetConverter;
import com.picus.core.old.global.common.converter.StringSetConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expert extends BaseEntity {

    @Id
    @Column(name = "expert_no")
    private Long id;

    private String intro; // 한 줄 자기소개

    @Column(nullable = false)
    private String career; // 경력, ex) 수상 내역, 교육 이수 내역 etc

    @Convert(converter = StringSetConverter.class)
    private Set<String> skills = new HashSet<>(); // 보유 기술, ex) 포토샵, 일러스트레이터, 파워포인트, 카메라 스펙 etc

    @Convert(converter = ActivityTypeSetConverter.class)
    private Set<ActivityType> activityTypes = new HashSet<>(); // 활동 유형, ex) 사진작가, 편집자

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus; // Expert 승인 상태

    @Convert(converter = DistrictSetConverter.class)
    private Set<District> activityAreas = new HashSet<>(); // 활동 구역

    private Expert(Long userId, String intro, String career, Set<String> skills, Set<District> activityAreas) {
        this.id = userId;
        this.intro = intro;
        this.career = career;
        this.skills = skills;
        this.approvalStatus = ApprovalStatus.PENDING;
        this.activityAreas = activityAreas;
    }

    public static Expert create(Long userNo, RegExpReq request) {
        return new Expert(userNo,
                request.intro(),
                request.career(),
                request.skills(),
                request.activityAreas());
    }

    public void updateActivityAreas(Set<District> areas) {
        this.activityAreas.addAll(areas);
    }

    public void updateActivityType(ActivityType activityType) {
        this.activityTypes.add(activityType);
    }


}
