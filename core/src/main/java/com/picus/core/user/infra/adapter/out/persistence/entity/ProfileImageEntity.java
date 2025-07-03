package com.picus.core.user.infra.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProfileImageEntity {

    @Id @Tsid
    private String profileImageNo;

    @NotBlank(message = "key 값은 필수입니다.")
    private String key;

    @NotBlank(message = "userNo는 공백일 수 없습니다.")
    private String userNo;

}
