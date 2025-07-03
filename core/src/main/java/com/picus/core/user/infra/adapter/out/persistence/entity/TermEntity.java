package com.picus.core.user.infra.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class TermEntity {

    @Id @Tsid
    private String termNo;

    @NotBlank(message = "약관명은 필수입니다.")
    private String name;

    @NotBlank(message = "약관 내용은 필수입니다.")
    private String content;

    @NotNull(message = "isRequired 값은 null일 수 없습니다.")
    private Boolean isRequired;

    @PrePersist
    protected void init() {
        if (this.isRequired == null) {
            this.isRequired = Boolean.FALSE;
        }
    }
}
