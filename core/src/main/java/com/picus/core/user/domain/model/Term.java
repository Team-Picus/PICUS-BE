package com.picus.core.user.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Term {

    private String termNo;
    private String name;
    private String content;
    private Boolean isRequired;

}
