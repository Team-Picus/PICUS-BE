package com.picus.core.price.adapter.out.persistence.entity;

import com.picus.core.expert.adapter.out.persistence.converter.StringConverter;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@Getter
@Table(name = "packages")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class PackageEntity {

    @Id @Tsid
    private String packageNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_no", nullable = false)
    private PriceEntity priceEntity;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Convert(converter = StringConverter.class)
    @Column(nullable = false)
    private List<String> contents = new ArrayList<>();

    private String notice;

    public void assignPriceEntity(PriceEntity priceEntity) {
        this.priceEntity = priceEntity;
    }

    public void updateEntity(String name, int price, List<String> contents, String notice) {
        this.name = name;
        this.price = price;
        this.contents = contents;
        this.notice = notice;
    }
}
