package com.picus.core.old.global.oauth.entity;

import com.picus.core.old.global.common.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class RefreshToken extends BaseEntity {

    @Id
    private Long userNo;

    @NotNull
    private String refreshToken;

    public RefreshToken(@NotNull Long userNo, @NotNull String refreshToken) {
        this.userNo = userNo;
        this.refreshToken = refreshToken;
    }
}
