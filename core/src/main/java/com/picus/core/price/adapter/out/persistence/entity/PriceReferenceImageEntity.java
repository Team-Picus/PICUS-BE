package com.picus.core.price.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "price_reference_images")
@NoArgsConstructor(access = PROTECTED)
public class PriceReferenceImageEntity {

    @Id @Tsid
    private Long priceReferenceImageNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_no", nullable = false)
    private PriceEntity priceEntity;

    @Column(nullable = false)
    private String fileKey;

    @Column(nullable = false)
    private Integer imageOrder;
}
