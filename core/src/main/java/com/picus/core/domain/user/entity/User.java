package com.picus.core.domain.user.entity;

import com.picus.core.domain.user.entity.profile.Profile;
import com.picus.core.domain.user.entity.withdrawal.Withdrawal;
import com.picus.core.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@SecondaryTable(
        name = "withdrawal",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_no")
)
public class User extends BaseEntity {

    @Id @Tsid
    @Column(name = "user_no")
    private Long id;

    private Profile profile;

    @AttributeOverrides({
            @AttributeOverride(
                    name = "withdrawalAt",
                    column = @Column(table = "withdrawal", name = "withdrawal_at"))
            // 탈퇴 사유 추가 가능성 고려
    })
    @Embedded
    private Withdrawal withdrawal;
}
