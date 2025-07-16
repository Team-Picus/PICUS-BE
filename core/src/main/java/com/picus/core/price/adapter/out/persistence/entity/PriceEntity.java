package com.picus.core.price.adapter.out.persistence.entity;

import com.picus.core.expert.domain.model.vo.PriceThemeType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "prices")
@NoArgsConstructor(access = PROTECTED)
public class PriceEntity {

    @Id @Tsid
    private String priceNo;

    @Column(nullable = false)
    private String expertNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PriceThemeType priceThemeType;

}
