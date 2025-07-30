package com.picus.core.follow.adapter.out.persistence.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FollowId implements Serializable {

    @EqualsAndHashCode.Include
    private String userNo;

    @EqualsAndHashCode.Include
    private String expertNo;

    public FollowId(String userNo, String expertNo) {
        this.userNo = userNo;
        this.expertNo = expertNo;
    }
}