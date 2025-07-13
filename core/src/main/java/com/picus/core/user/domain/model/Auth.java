package com.picus.core.user.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Auth {

    private String providerId;
    private Provider provider;

}
