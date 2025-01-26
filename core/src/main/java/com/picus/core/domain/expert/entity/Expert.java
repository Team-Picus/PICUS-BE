package com.picus.core.domain.expert.entity;

import com.picus.core.global.common.enums.ApprovalStatus;
import com.picus.core.global.common.enums.Area;
import com.picus.core.global.converter.StringSetConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expert {

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

    @Column(nullable = false)
    private Set<Area> area;

    private ApprovalStatus approvalStatus;

    public Expert(String intro, String career, Set<String> skills, Set<ActivityType> type, Set<Area> area) {
        this.intro = intro;
        this.career = career;
        this.skills = skills;
        this.type = type;
        this.area = area;
        this.approvalStatus = ApprovalStatus.PENDING;
    }
}
