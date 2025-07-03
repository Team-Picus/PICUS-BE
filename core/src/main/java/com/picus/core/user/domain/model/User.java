package com.picus.core.user.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {

    private String userNo;
    private String name;
    private Role role;
    private String nickname;
    private String email;
    private String tel;
    private Integer reservationHistoryCount;
    private Integer followCount;
    private Integer myMoodboardCount;
    private String expertNo;

}
