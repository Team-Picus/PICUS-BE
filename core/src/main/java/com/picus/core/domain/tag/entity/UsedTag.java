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
public class UsedTag {

    @Id
    private Long expertNo;

    @Id
    private Long tagId;

    public UsedTag(Long expertNo, Long tagId) {
        this.expertNo = expertNo;
        this.tagId = tagId;
    }
}
