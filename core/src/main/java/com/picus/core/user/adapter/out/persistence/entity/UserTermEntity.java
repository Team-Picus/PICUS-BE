package com.picus.core.user.adapter.out.persistence.entity;

import com.picus.core.shared.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "user_term")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@IdClass(UserTermId.class)
public class UserTermEntity extends BaseEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private UserEntity user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_no", nullable = false)
    private TermEntity term;

    @Column(nullable = false)
    private Boolean isAgreed;
}