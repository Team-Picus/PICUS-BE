package com.picus.core.user.adapter.out.persistence.entity;

import com.picus.core.shared.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "term")
public class TermEntity extends BaseEntity {

    @Id @Tsid
    private String termNo;

    @NotBlank(message = "약관명은 필수입니다.")
    private String name;

    @NotBlank(message = "약관 내용은 필수입니다.")
    private String content;

    @Column(nullable = false)
    private Boolean isRequired;

    @PrePersist
    protected void init() {
         if (this.isRequired == null) {
            this.isRequired = Boolean.FALSE;
        }
    }
}
