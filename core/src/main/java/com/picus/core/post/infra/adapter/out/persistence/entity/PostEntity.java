package com.picus.core.post.infra.adapter.out.persistence.entity;

import com.picus.core.expert.infra.adapter.out.persistence.entity.ExpertEntity;
import com.picus.core.post.domain.model.vo.PostMoodType;
import com.picus.core.post.domain.model.vo.PostStatus;
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

    @Column(nullable = false)
    private String expertNo;
    @Column(nullable = false)
    private String packageNo;

    private String title;

    private String oneLineDescription;

    private String detailedDescription;

    @Convert(converter = PostThemeTypeConverter.class)
    private List<PostThemeType> postThemeTypes = new ArrayList<>();

    @Convert(converter = PostMoodTypeConverter.class)
    private List<PostMoodType> postMoodTypes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SpaceType spaceType;

    private String spaceAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus postStatus;

    @Column(nullable = false)
    private Boolean isPinned;

}

