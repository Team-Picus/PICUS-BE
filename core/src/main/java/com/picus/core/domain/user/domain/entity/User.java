package com.picus.core.domain.user.domain.entity;

import com.picus.core.domain.user.domain.entity.profile.Profile;
import com.picus.core.domain.user.domain.entity.withdrawal.Withdrawal;
import com.picus.core.global.common.base.BaseEntity;
import com.picus.core.global.oauth.entity.Provider;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Table(name = "users")
@SecondaryTable(
        name = "withdrawal",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_no")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User extends BaseEntity {

    @Id @Tsid
    @Column(name = "user_no")
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private Profile profile;

    @Column(name = "provider", nullable = false)
    private Provider provider;

    @Column(name = "provider_id", nullable = false, unique = true)
    private String providerId;

    @AttributeOverrides({
            @AttributeOverride(
                    name = "withdrawalAt",
                    column = @Column(table = "withdrawal", name = "withdrawal_at"))
            // 탈퇴 사유 추가 가능성 고려
    })
    @Embedded
    private Withdrawal withdrawal;


    public void updateProfile(String nickname, Long profileImgId) {
        profile.updateProfile(nickname, profileImgId);
    }
}
