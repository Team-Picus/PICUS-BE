package com.picus.core.domain.user.entity.profile;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Column(length = 10)
    private String name;  // 이름
    // TODO nickname 추가로 가져올 것인지?

    @Column(length = 10, nullable = false)
    private String nickname;

    @Column(length = 15)
    private String tel;

    @Email
    @Column(length = 20)
    private String email;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String profileImgUrl;

    public Profile(String name, String nickname, String tel, String email, Gender gender, String profileImgUrl) {
        this.name = name;
        this.nickname = nickname;
        this.tel = tel;
        this.email = email;
        this.gender = gender;
        this.profileImgUrl = profileImgUrl;
    }
}
