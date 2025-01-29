package com.picus.core.domain.studio.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UsedCategoryId implements Serializable {

    @EqualsAndHashCode.Include
    private Long studioNo;

    @EqualsAndHashCode.Include
    private Long categoryId;

    public UsedCategoryId(Long studioNo, Long categoryId) {
        this.studioNo = studioNo;
        this.categoryId = categoryId;
    }
}
