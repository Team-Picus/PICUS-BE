<<<<<<<< HEAD:core/src/main/java/com/picus/core/domain/studio/domain/entity/category/UsedCategoryId.java
package com.picus.core.domain.studio.domain.entity.category;
========
package com.picus.core.domain.studio.domain.entity;
>>>>>>>> feat/#26/Post-도메인-개발:core/src/main/java/com/picus/core/domain/studio/domain/entity/UsedCategoryId.java

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
