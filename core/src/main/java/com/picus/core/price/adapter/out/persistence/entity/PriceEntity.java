package com.picus.core.price.adapter.out.persistence.entity;

import com.picus.core.expert.domain.model.vo.PriceThemeType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@Getter
@Table(name = "prices")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class PriceEntity {

    @Id
    @Tsid
    private String priceNo;

    @Column(nullable = false)
    private String expertNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PriceThemeType priceThemeType;

    public void updateEntity(PriceThemeType priceThemeType) {
        this.priceThemeType = priceThemeType;
    }

}
