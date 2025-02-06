package com.picus.core.domain.user.entity;

import com.picus.core.domain.user.entity.profile.Profile;
import com.picus.core.domain.user.entity.withdrawal.Withdrawal;
import com.picus.core.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
