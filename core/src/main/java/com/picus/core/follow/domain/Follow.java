package com.picus.core.follow.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Follow {

    private String userNo;
    private String expertNo;
    private LocalDateTime followedAt;
}
