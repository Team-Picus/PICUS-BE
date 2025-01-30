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
    private Long studio;

    @EqualsAndHashCode.Include
    private Long category;

    public UsedCategoryId(Long studio, Long category) {
        this.studio = studio;
        this.category = category;
    }
}
