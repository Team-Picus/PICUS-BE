package com.picus.core.reservation.adapter.out.persistence.entity.vo;

import com.picus.core.expert.adapter.out.persistence.converter.StringConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Embeddable
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PackageSnapshot {

    private String name;
    private Integer price;
    private String notice;
    @Convert(converter = StringConverter.class)
    private List<String> contents;
}
