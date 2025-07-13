package com.picus.core.expert.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Studio {
    private String studioName;
    private Integer employeesCount;
    private String businessHours;
    private String address;
}
