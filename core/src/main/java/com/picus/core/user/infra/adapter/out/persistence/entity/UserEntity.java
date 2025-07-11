package com.picus.core.user.infra.adapter.out.persistence.entity;

import com.picus.core.shared.common.BaseEntity;
import com.picus.core.user.domain.model.Role;
import com.picus.core.user.domain.model.SocialType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_users_nickname", columnNames = "nickname"),
                @UniqueConstraint(name = "uk_users_provider", columnNames = {"provider", "providerId"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserEntity extends BaseEntity {

    @Id @Tsid
    private String userNo;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false, length = 16)
    private String tel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private Integer reservationHistoryCount;

    @Column(nullable = false)
    private Integer followCount;

    @Column(nullable = false)
    private Integer myMoodboardCount;

    private String expertNo;    // FK (nullable)

    @PrePersist
    protected void init() {
        if (reservationHistoryCount == null)
            reservationHistoryCount = 0;
        if (followCount == null)
            followCount = 0;
        if (myMoodboardCount == null)
            myMoodboardCount = 0;
    }
}
