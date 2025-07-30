package com.picus.core.post.adapter.out.persistence.entity;

import com.picus.core.post.adapter.out.persistence.converter.PostMoodTypeConverter;
import com.picus.core.post.adapter.out.persistence.converter.PostThemeTypeConverter;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SpaceType;
import com.picus.core.shared.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.*;
import static lombok.AccessLevel.PRIVATE;

@SuperBuilder
@Getter
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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String oneLineDescription;

    private String detailedDescription;

    @Convert(converter = PostThemeTypeConverter.class)
    @Builder.Default
    private List<PostThemeType> postThemeTypes = new ArrayList<>();

    @Convert(converter = PostMoodTypeConverter.class)
    @Builder.Default
    private List<PostMoodType> postMoodTypes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SpaceType spaceType;

    @Column(nullable = false)
    private String spaceAddress;

    @Column(nullable = false)
    private Boolean isPinned;

    public void updatePostEntity(
            String packageNo, String title, String oneLineDescription, String detailedDescription,
            List<PostThemeType> postThemeTypes, List<PostMoodType> postMoodTypes,
            SpaceType spaceType, String spaceAddress, Boolean isPinned
    ) {
        this.title = title;
        this.oneLineDescription = oneLineDescription;
        this.detailedDescription = detailedDescription;
        this.postThemeTypes = postThemeTypes;
        this.postMoodTypes = postMoodTypes;
        this.spaceType = spaceType;
        this.spaceAddress = spaceAddress;
        this.packageNo = packageNo;
        this.isPinned = isPinned;
    }

}

