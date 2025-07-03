package com.picus.core.follow.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Follow {

    private String followNo;
    private String followerNo;  // clientNo
    private String followeeNo;  // expertNo
    private LocalDateTime followedAt;
}
