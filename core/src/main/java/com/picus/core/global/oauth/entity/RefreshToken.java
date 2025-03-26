package com.picus.core.global.oauth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.picus.core.global.common.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
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
