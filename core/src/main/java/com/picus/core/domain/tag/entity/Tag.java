package com.picus.core.domain.tag.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    private String name;

    private TagType tagType;

    private Tag(String name, TagType tagType) {
        this.name = name;
        this.tagType = tagType;
    }

    public static Tag createForAdmin(String name, TagType tagType) {
        return new Tag(name, tagType);
    }
}
