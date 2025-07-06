package com.picus.core.user.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileImage {

    private String profileImageNo;
    private String key;
    private String userNo;

}
