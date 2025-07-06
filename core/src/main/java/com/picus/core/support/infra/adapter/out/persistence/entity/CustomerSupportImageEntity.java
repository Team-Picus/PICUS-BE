package com.picus.core.support.infra.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_support_images")
public class CustomerSupportImageEntity {

    @Id @Tsid
    private String customerSupportImageNo;

    private String key;
}
