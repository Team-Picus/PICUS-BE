package com.picus.core.post.infra.adapter.out.persistence.entity;

import com.picus.core.expert.infra.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.post.domain.model.vo.PostMoodType;
import com.picus.core.post.domain.model.vo.PostThemeType;
import com.picus.core.post.domain.model.vo.SpaceType;
import com.picus.core.post.infra.adapter.out.persistence.converter.PostMoodTypeConverter;
import com.picus.core.post.infra.adapter.out.persistence.converter.PostThemeTypeConverter;
import com.picus.core.price.infra.adapter.out.persistence.entity.PackageEntity;
import com.picus.core.shared.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends BaseEntity {

    @Id @Tsid
    private String postNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_no")
    private ExpertEntity expertEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_no")
    private PackageEntity packageEntity;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String oneLineDescription;
    @Column(nullable = false)
    private String detailedDescription;
    @Convert(converter = PostThemeTypeConverter.class)
    @Column(nullable = false)
    private List<PostThemeType> postThemeTypes = new ArrayList<>();
    @Convert(converter = PostMoodTypeConverter.class)
    @Column(nullable = false)
    private List<PostMoodType> postMoodTypes = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpaceType spaceType;
    @Column(nullable = false)
    private String spaceAddress;

}

