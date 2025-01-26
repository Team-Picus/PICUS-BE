package com.picus.core.domain.expert.entity;

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

    @Convert(converter = StringSetConverter.class)  // 추후 스킬로 필터링 및 검색 기준이 되지 않을 것이므로 별도의 테이블이 아닌 컨버터 사용
    private Set<String> skills = new HashSet<>();

    @Column(nullable = false)
    private Set<ActivityType> type = new HashSet<>();

    @Column(nullable = false)
    private Set<Area> area;

    public Expert(String intro, String career, Set<String> skills, Set<ActivityType> type, Set<Area> area) {
        this.intro = intro;
        this.career = career;
        this.skills = skills;
        this.type = type;
        this.area = area;
    }
}
