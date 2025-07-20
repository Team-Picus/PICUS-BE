package com.picus.core.expert.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class Studio {
    private String studioNo;
    private String studioName;
    private Integer employeesCount;
    private String businessHours;
    private String address;
}
