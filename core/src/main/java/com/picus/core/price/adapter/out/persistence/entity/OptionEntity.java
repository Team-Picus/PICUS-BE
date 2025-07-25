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
@Table(name = "options")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class OptionEntity {

    @Id @Tsid
    private String optionNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_no", nullable = false)
    private PriceEntity priceEntity;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer count;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    @Convert(converter = StringConverter.class)
    private List<String> contents = new ArrayList<>();

    public void assignPriceEntity(PriceEntity priceEntity) {
        this.priceEntity = priceEntity;
    }

    public void updateEntity(String name, int count, int price, List<String> contents) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.contents = contents;
    }
}
