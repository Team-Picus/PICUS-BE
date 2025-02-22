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

    private String intro;

    @Column(nullable = false)
    private String career;

    @Convert(converter = StringSetConverter.class)
    private Set<String> skills = new HashSet<>();

    @Column(nullable = false)
    private Set<ActivityType> type = new HashSet<>();

    private ApprovalStatus approvalStatus;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "expert")
    private List<ExpertDistrict> activityAreas = new ArrayList<>();

    public Expert(Long userId, String intro, String career, Set<String> skills, Set<ActivityType> type) {
        this.id = userId;
        this.intro = intro;
        this.career = career;
        this.skills = skills;
        this.type = type;
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
