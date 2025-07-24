package com.picus.core.user.adapter.out.persistence.entity;

import com.picus.core.shared.common.BaseEntity;
import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.Role;
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

    @Column(length = 30)
    private String name;

    @Column(length = 10)
    private String nickname;

    @Column(length = 16)
    private String tel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String email;

    @Column(nullable = false)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

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
        if (role == null)
            role = Role.CLIENT;
    }

    public void updateSocialProfile(String email, String name, String tel) {
        if(this.email == null)
            this.email = email;
        if(this.name == null)
            this.name = name;
        if(this.tel == null)
            this.tel = tel;
    }

    public void assignExpertNo(String expertNo) {
        this.expertNo = expertNo;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
