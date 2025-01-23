package com.picus.core.domain.tag.entity;

import com.picus.core.domain.term.entity.UserTermId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@IdClass(UserTermId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTag {

    @Id
    private Long postNo;

    @Id
    private Long tagId;

    public PostTag(Long postNo, Long tagId) {
        this.postNo = postNo;
        this.tagId = tagId;
    }
}
