package com.picus.core.domain.expert.entity;

import com.picus.core.domain.expert.entity.area.ExpertDistrict;
import com.picus.core.global.common.base.BaseEntity;
import com.picus.core.global.common.enums.ApprovalStatus;
import com.picus.core.global.common.converter.StringSetConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @Column(nullable = false)
    private Set<ActivityType> activityTypes = new HashSet<>(); // 활동 유형, ex) 사진작가, 편집자

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus; // Expert 승인 상태

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "expert")
    private List<ExpertDistrict> activityAreas = new ArrayList<>(); // 활동 구역

    public Expert(Long userId, String intro, String career, Set<String> skills, Set<ActivityType> activityTypes) {
        this.id = userId;
        this.intro = intro;
        this.career = career;
        this.skills = skills;
        this.activityTypes = activityTypes;
        this.approvalStatus = ApprovalStatus.PENDING;
    }

    public void updateActivityAreas(List<ExpertDistrict> areas) {
        for (ExpertDistrict area : areas) {
            if (!this.activityAreas.contains(area)) {
                this.activityAreas.add(area);
            }
        }

        this.activityAreas.removeIf(existingArea -> !areas.contains(existingArea));
    }
}
