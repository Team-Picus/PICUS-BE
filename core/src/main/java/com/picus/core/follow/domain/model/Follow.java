package com.picus.core.follow.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Follow {

    private String userNo;  // clientNo
    private String expertNo;  // expertNo
    private LocalDateTime followedAt;
}
