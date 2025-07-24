package com.picus.core.post.adapter.out.persistence.entity;

import com.picus.core.post.adapter.out.persistence.converter.PostMoodTypeConverter;
import com.picus.core.post.adapter.out.persistence.converter.PostThemeTypeConverter;
import com.picus.core.post.domain.model.vo.PostMoodType;
import com.picus.core.post.domain.model.vo.PostStatus;
import com.picus.core.post.domain.model.vo.PostThemeType;
import com.picus.core.post.domain.model.vo.SpaceType;
import com.picus.core.shared.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.*;
import static lombok.AccessLevel.PRIVATE;

@Builder
@Entity
@Table(name = "posts")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
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

    @Column(nullable = false)
    private Boolean isPinned;

}

