package com.picus.core.domain.studio.domain.entity.category;

import com.picus.core.domain.studio.domain.entity.Studio;
import com.picus.core.global.common.base.BaseEntity;
import com.picus.core.domain.shared.category.entity.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@IdClass(UsedCategoryId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsedCategory extends BaseEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "studio_no", nullable = false)
    private Studio studio;

    @Enumerated(EnumType.STRING)
    private Category category;

    public UsedCategory(Studio studio, Category category) {
        this.studio = studio;
        this.category = category;
    }
}
