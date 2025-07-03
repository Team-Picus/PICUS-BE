package com.picus.core.user.infra.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@IdClass(UserTermId.class)
public class UserTermEntity {

    @Id
    private String userNo;

    @Id
    private String termNo;

    @NotNull(message = "isAgreed 값은 null일 수 없습니다.")
    private Boolean isAgreed;

    @PastOrPresent(message = "agreedAt은 과거 또는 현재 시각이어야 합니다.")
    private LocalDateTime agreedAt;

    @PrePersist
    protected void init() {
        if (Boolean.TRUE.equals(this.isAgreed) && this.agreedAt == null) {
            this.agreedAt = LocalDateTime.now();
        }
    }
}

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class UserTermId implements Serializable {

    @EqualsAndHashCode.Include
    private Long userNo;

    @EqualsAndHashCode.Include
    private Long termNo;

    public UserTermId(Long userNo, Long termNo) {
        this.userNo = userNo;
        this.termNo = termNo;
    }
}