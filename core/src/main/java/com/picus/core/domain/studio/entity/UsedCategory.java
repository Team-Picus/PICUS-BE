package com.picus.core.domain.studio.entity;

import com.picus.core.global.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@IdClass(UsedCategoryId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsedCategory extends BaseEntity {

    @Id
    private Long studioNo;

    @Id
    private Long categoryId;

    public UsedCategory(Long studioNo, Long categoryId) {
        this.studioNo = studioNo;
        this.categoryId = categoryId;
    }
}
