package com.picus.core.user.infra.adapter.out.persistence.entity;

import com.picus.core.shared.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserEntity extends BaseEntity {

    @Id @Tsid
    private String userNo;

    @NotBlank(message = "이름은 필수입니다.")
    @Length(max = 30, message = "이름은 30자를 초과할 수 없습니다.")
    private String name;

    @NotNull(message = "역할은 필수입니다.")
    private Role role;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Length(max = 10, message = "닉네임은 10자를 초과할 수 없습니다.")
    private String nickname;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Length(min = 6, max = 16, message = "전화번호는 6~16자리여야 합니다.")
    @Pattern(regexp = "^[0-9]{6,16}$", message = "전화번호는 숫자만 포함해야 합니다.")
    private String tel;

    private Integer reservationHistoryCount;

    private Integer followCount;

    private Integer myMoodboardCount;

    private String expertNo;    // FK

    @NotNull(message = "socialType은 필수입니다.")
    private SocialType socialType;

    @NotBlank(message = "providerId은 필수입니다.")
    private String providerId;

    @NotBlank(message = "provider는 필수입니다.")
    private String provider;

    @PrePersist
    protected void init() {
        this.reservationHistoryCount = 0;
        this.followCount = 0;
        this.myMoodboardCount = 0;
    }
}
