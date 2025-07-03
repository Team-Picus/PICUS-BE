package com.picus.core.user.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Auth {

    private String userNo;
    private SocialType socialType;
    private String providerId;
    private String provider;

}
