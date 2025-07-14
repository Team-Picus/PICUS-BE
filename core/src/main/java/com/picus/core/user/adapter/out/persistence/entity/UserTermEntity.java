package com.picus.core.user.adapter.out.persistence.entity;

import com.picus.core.shared.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_term")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@IdClass(UserTermId.class)
public class UserTermEntity extends BaseEntity {

    @Id
    private String userNo;

    @Id
    private String termNo;

    @Column(nullable = false)
    private Boolean isAgreed;
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