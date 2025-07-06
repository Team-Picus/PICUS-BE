package com.picus.core.post.infra.adapter.out.persistence.entity;

import com.picus.core.post.domain.model.vo.PostMoodType;
import com.picus.core.post.domain.model.vo.PostThemeType;
import com.picus.core.post.domain.model.vo.SpaceType;
import com.picus.core.post.infra.adapter.out.persistence.converter.PostMoodTypeConverter;
import com.picus.core.post.infra.adapter.out.persistence.converter.PostThemeTypeConverter;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class PostEntity {

    @Id @Tsid
    private String postNo;

    private String title;
    private String content;
    @Convert(converter = PostThemeTypeConverter.class)
    private List<PostThemeType> postThemeTypes = new ArrayList<>();
    @Convert(converter = PostMoodTypeConverter.class)
    private List<PostMoodType> postMoodTypes = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private SpaceType spaceType;
    private String spaceAddress;

}
