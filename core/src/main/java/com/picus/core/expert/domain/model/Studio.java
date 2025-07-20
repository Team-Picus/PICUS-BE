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

    public void updateStudio(String studioName, Integer employeesCount, String businessHours, String address) {
        if(studioName != null)
            this.studioName = studioName;
        if(employeesCount != null)
            this.employeesCount = employeesCount;
        if(businessHours != null)
            this.businessHours = businessHours;
        if(address != null)
            this.address = address;
    }
}
