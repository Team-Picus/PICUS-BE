package com.picus.core.price.adapter.out.persistence.entity;

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
@Table(name = "price_reference_images")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class PriceReferenceImageEntity {

    @Id @Tsid
    private String priceReferenceImageNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_no", nullable = false)
    private PriceEntity priceEntity;

    @Column(nullable = false)
    private String fileKey;

    @Column(nullable = false)
    private Integer imageOrder;

    public void assignPriceEntity(PriceEntity priceEntity) {
        this.priceEntity = priceEntity;
    }
}
