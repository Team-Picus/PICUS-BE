package com.picus.core.user.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserTerm {

    private String userNo;
    private String termNo;
    private Boolean isAgreed;
    private LocalDateTime agreedAt;

}
